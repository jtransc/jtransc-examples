package example;

import com.jtransc.JTranscSystem;
import com.jtransc.JTranscVersion;
import com.jtransc.annotation.JTranscAddDefines;
import com.jtransc.annotation.JTranscAddTemplateVars;

public class Test {
    static public void main(String[] args) {
        VitaSdk.init();
        System.out.println("Hello World! " + JTranscVersion.getVersion() + ", " + JTranscVersion.getRuntime() + ", " + JTranscSystem.usingJTransc());
    }
}