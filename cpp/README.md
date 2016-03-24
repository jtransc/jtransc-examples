WWX 2014 + WWX 2015

Controlling Build.xml
    Environment:
        HXCPP_VERBOSE=1
        HXCPP_LOAD_DEBUG=1
        HXCPP_CONFIG=myconfig.xml
    Defines:
        haxe compile.hxml -D HXCPP_CHECK_POINTER
        haxelib run hxcpp -DHXCPP_M64
        .hxcpp_config.xml:
        <xml>
            <section id="vars"/>
            <set name="SDK_ROOT" value="e:/SDK/" />
            ..
    Toolchains:
        haxe compile.hxml -lib myToolchain
        myToolchain/toolchain/setup.xml
        <xml>
            <set name="toolchain" value="myFile"/>
            <include name="${HXCPP_CONFIG}" section="vars" />
Level 1 Magic - untyped
    untyped:
        * Skips "most" type checking, but still needs valid syntax
        * Interface between haxe and external code
        * use with __global__ and __cpp__
    __global__ == ::
        useful because haxe does not have global namespace
         untyped __global__.MessageBox(0,"Hello","Title",0);
         Works because
            windows.h is included (on windows)
            0 = null pointer
            "Hello" = Auto-cast from haxe string
    __cpp__("")
        __cpp__('printf("Hello");');
        * parses as local function call
        * anything goes
        * but it is text substitution - so:
            * keyword substitution fails
            * inline fails
    cpp.NativeArray:
        using cpp.NativeArray;
        var array = new Array<Int>();
        array.unsafeSet(10,1);
        var a = array.unsafeGet(10);
        array.zero();
    cpp.String:
        using cpp.NativeString;
        trace("hello".c_str().at(1));
    cpp.Float32:
        import cpp.Float32;
        var f = new Array<Float>();
        trace(untyped __cpp__("(int)sizeof({0})", f[0]));
         var f = new Array<Float32>();
        trace(untyped __cpp__("(int)sizeof({0})", f[0]));
        // 8, 4
    cpp.UInt8:
        import cpp.UInt8;
        var array = new Array<UInt8>();
        array[0] = 256;
        trace(array[0]);
        // 1
Level 2 - Meta Magic:
    Class meta:
        Text, so anything goes
        @:headerClassCode(...); -> inject member variables/inline functions
        @:headerCode(...); -> include external headers
        @:headerNamespaceCode(...); -> declare globals in namespaces
        @:cppFileCode(...); -> include external headers only in cpp file
        @:cppNamespaceCode(...); -> implement static variables
    Function meta:
        @:functionCode(...);
        @:functionTailCode(...);
        Largely redundant (use __cpp__)
    @:buildXml
        @:buildXml('
            <target id="haxe">
                <lib name="${HXCPP}/lib/${BINDIR}/libstd${LIBEXTRA}${LIBEXT}"/>
                <lib name="wsock32.lib" if="windows"/>
            </target>
        ')
        @:keep class StaticStd

        Or, use one <include />
        @:buildXml("<include name='${haxelbi:nme}/lib/NmeLink.xml'/>")
        @:keep class StaticNme
Level 3 - Extern Magic:
    cpp.Pointer:
        import cpp.Pointer;
        * value, at, add, less than...
        * Pointer to external class
        * Pointer to haxe variable (with care)
        * Be careful - does not hold GC reference
        import cpp.Function
        * Pointer to static function (not closure)
        var array = [1,2,3];
        var intPtr = Pointer.arrayElem(array, 1);
        trace(intPtr.value);
        trace(intPtr.add(1).value);
        // 2,3
    extern class
        @:include("string")
        @:include("./") relative to .hx file

        @:structAccess
        @:unreflective
        @:native("std::string")
        extern class StdString {
            @:native("new std::string")
            public static function create(inString:String):cpp.Pointer<StdString>;
            public function size():Int;
            public function find(str:String):Int;
        }
        var std = StdString.create("My std::string");
        trace(std.value.size());
        std.destroy();
        Warning - non-Pointer version suitable for member variable
    Pointer hacks:
        public static function f_inv_square(number:Float):Float {
            var y:Float32 = number;
            var intPtr:Pointer<Int> = Pointer.addressOf(y).reinterpret();
            intPtr.ref = 0x5f3759df - (intPtr.ref >> 1);
            var result = y*(1.5 - (number * 0.5 * y * y));
            trace(result + " close to " + Math.sqrt(1.0/number));
            return result;
        }
    CFFI data:
        Five point plan for direct access to structures
        allocated with "alloc_abstract"

        1 Define externs
          extern class MyData { }
        2 Force inclusion of header
          @:buildXml( ... -I${haxelib:mylib})
          @:include("mylib.h")
        3 Add link libraries
          @:buildXml( ... <lib> )
          OR use virtual functions
          struct MyData { virtual void someFunc(); }
        4 Extract pointer from abstract handle:
          var myData:Pointer<MyData> = Pointer.fromHandle(handle);
        5 Use:
          myData.value.someFunc();

Level 4 - Necromancy:
    finalizer:
        class Test {
            var handle:String;
            public function new() {
                handle = "free me";
                cpp.vm.Gc.setFinalizer(this,cpp.Function.fromStaticFunction(destroy));
            }
            @:void public static function destroy(inTest:Test):Void {
                untyped __cpp__('printf("Free %s\\n", inTest->handle.__s)');
            }
            static function createLeak() {
                for (i in 0 ... 10) new Test();
            }
            static public function main() {
                createLeak();
                cpp.vm.Gc.run(true);
            }
        }
        * Can't make GC calls
        * Wrong thread
    zombies:
        class Test {
            var handle:String;
            public function new() {
                handle = "free me";
                cpp.vm.Gc.doNotKill(this);
            }
            public static function destroy(inTest:Test): Void {
                trace('Free ' + inTest);
            }
            static function createLeak() {
                for (i in 0 ... 10) new Test();
            }
            static public function main() {
                createLeak();
                cpp.vm.Gc.run(true);
                while (true) {
                    var zombie = cpp.vm.Gc.getNextZombie();
                    if (zombie == null) break;
                    if (Std.is(zombie, Test)) destroy(zombie);
                }
            }
        }
        * Control the timing (correct thread)
        * Can "reanimate"
        * Can call Gc function
        * Mght be delayed (next "ENTER_FRAME")
More Tips:
    * @:include("./") relative to .hx file
    * @:native static functions = global functions
    * @:cppInclude glue/api code from a local cpp file
    @:include("./../../include/MyApi.h")
    @:cppInclude("MyApiGlue.cpp")
    extern class SomeApi {
        @:native("some_api_do_something")
        public static function doSomething():Void;
    }
    SomeApi.doSomething();
Advantages:
    * Struct creates values on the stack (no GC)
    * Works with reflection/Dynamic
    * Allows for natural syntax
    * A better way of writting (small) native extensions
       * No external libraries are required.
       * Can generate ".mm" code for iOS extensions -D file-extension=mm
Cppia compiling:
    * Output directory becomes output file
    * Both "cpp" and "cppia" defined
    * Can't use __cpp__ or native features
    * Generate a file looks like an AST dump
      * debug version is ascii
      * release version is binary
    haxe -main Test -cpp test.cppia -D cppia
    haxelib run hxcpp test.cppia
