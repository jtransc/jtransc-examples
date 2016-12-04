import com.jtransc.annotation.JTranscKeep;
import com.jtransc.dynarec.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.jtransc.dynarec.builder.FunctionBuilder.*;

public class BrainfuckDynarec {
    static public void main(String[] args) {
        String helloWorld = "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.";
        FunctionCompiler suitableCompiler = FunctionCompilers.getSuitableCompiler();
        Function function = FUNCTION(BrainfuckCompiler.generateAst(helloWorld));
        AnyInvoke invokable = suitableCompiler.compile(function);

        //BrainfuckRuntime.putchar('!');
        //System.out.println(JavascriptCompiler.generateCode(function));

        invokable.invoke();
    }

    @JTranscKeep
    static public class BrainfuckRuntime {
        static public void putchar(int c) {
            System.out.print((char) c);
        }
    }

    static class BrainfuckCompiler {
        private Local array = new Local();
        private Local ptr = new Local();
        private Expr arrayExpr = LOCAL(array);
        private Expr ptrExpr = LOCAL(ptr);
        private int n;
        private String str;
        private Method printchar = BrainfuckRuntime.class.getMethod("putchar", int.class);

        BrainfuckCompiler() throws NoSuchMethodException {
        }

        static public Stm generateAst(String str) {
            try {
                return new BrainfuckCompiler().compileFull(str);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        private Stm compileFull(String str) {
            this.n = 0;
            this.str = str;
            return STMS(
                    SETLOCAL(array, NEWARRAY(Integer.TYPE, INT(10000))),
                    SETLOCAL(ptr, INT(0)),
                    compileChunk()
            );
        }

        private Stm compileChunk() {
            ArrayList<Stm> stms = new ArrayList<Stm>();
            loop:
            for (; n < str.length(); n++) {
                // (Program Start)	char array[infinitely large size] = {0};
                // char *ptr=array;
                // >	++ptr;
                // <	--ptr;
                // +	++*ptr;
                // -	--*ptr;
                // .	putchar(*ptr);
                // ,	*ptr=getchar();
                // [	while (*ptr) {
                // ]	}

                switch (str.charAt(n)) {
                    case '>':
                        stms.add(SETLOCAL(ptr, IADD(ptrExpr, INT(1))));
                        break;
                    case '<':
                        stms.add(SETLOCAL(ptr, ISUB(ptrExpr, INT(1))));
                        break;
                    case '+':
                        stms.add(SETARRAY(arrayExpr, ptrExpr, IADD(GETARRAY(arrayExpr, ptrExpr), INT(1))));
                        break;
                    case '-':
                        stms.add(SETARRAY(arrayExpr, ptrExpr, ISUB(GETARRAY(arrayExpr, ptrExpr), INT(1))));
                        break;
                    case '.':
                        stms.add(STM(CALLSTATIC(printchar, GETARRAY(arrayExpr, ptrExpr))));
                        break;
                    case ',':
                        throw new RuntimeException("Not implemented input!");
                    case '[':
                        n++;
                        Stm body = compileChunk();
                        stms.add(WHILE(NE(GETARRAY(arrayExpr, ptrExpr), INT(0)), body));
                        break;
                    case ']':
                        break loop;
                    default:
                        // ignore
                        break;
                }
            }
            return STMS(stms.toArray(new Stm[0]));
        }
    }
}
