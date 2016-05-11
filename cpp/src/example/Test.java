package example;

import com.jtransc.annotation.haxe.HaxeAddFilesTemplate;
import com.jtransc.annotation.haxe.HaxeMeta;
import com.jtransc.annotation.haxe.HaxeMethodBody;

public class Test {
    static public void main(String[] args) {
        System.out.println(Demo.mysum(7, 3));
    }
}

@HaxeMeta("@:cppInclude('./../test.c')")
@HaxeAddFilesTemplate("test.c")
class Demo {
    @HaxeMeta("@:noStack")
    @HaxeMethodBody(target = "cpp", value = "return untyped __cpp__('::sum({0}, {1})', p0, p1);")
    static native public int mysum(int a, int b);
}
