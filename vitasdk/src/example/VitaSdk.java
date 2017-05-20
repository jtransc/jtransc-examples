package example;

import com.jtransc.annotation.JTranscAddDefines;
import com.jtransc.annotation.JTranscAddHeader;
import com.jtransc.annotation.JTranscAddLibraries;
import com.jtransc.annotation.JTranscAddTemplateVars;

@JTranscAddTemplateVars(variable = "CMAKE", target = "cpp", list = {
        "if(NOT DEFINED CMAKE_TOOLCHAIN_FILE)",
        "   if(DEFINED ENV{VITASDK})",
        "       set(CMAKE_TOOLCHAIN_FILE \"$ENV{VITASDK}/share/vita.toolchain.cmake\" CACHE PATH \"toolchain file\")",
        "   else()",
        "       message(FATAL_ERROR \"Please define VITASDK to point to your SDK path!\")",
        "   endif()",
        "endif()",
})
@JTranscAddTemplateVars(variable = "CMAKE_PROJECT", target = "cpp", list = {
        "include(\"$ENV{VITASDK}/share/vita.cmake\" REQUIRED)",
        "include_directories(/usr/local/vitasdk/include/)",
        "include_directories(/usr/local/vitasdk/arm-vita-eabi/include)",
        "link_directories(/usr/local/vitasdk/lib/)",
        "link_directories(/usr/local/vitasdk/arm-vita-eabi/lib/)",
        "set(VITA_APP_NAME \"Hello World\")",
        "set(VITA_TITLEID  \"VSDK00006\")",
        "set(VITA_VERSION  \"01.00\")",

        "vita_create_self(program.self program)",
        "vita_create_vpk(program.vpk ${VITA_TITLEID} program.self",
        "   VERSION ${VITA_VERSION}",
        "   NAME ${VITA_APP_NAME}",
        //"FILE sce_sys/icon0.png sce_sys/icon0.png",
        //"FILE sce_sys/livearea/contents/bg.png sce_sys/livearea/contents/bg.png",
        //"FILE sce_sys/livearea/contents/startup.png sce_sys/livearea/contents/startup.png",
        //"FILE sce_sys/livearea/contents/template.xml sce_sys/livearea/contents/template.xml",
        ")"

})
@JTranscAddHeader(target = "cpp", value = {
        "extern \"C\" {",
        "#include <psp2/io/dirent.h>",
        "#include <psp2/ctrl.h>",
        "#include <psp2/touch.h>",
        "#include <psp2/display.h>",
        "#include <psp2/gxm.h>",
        "#include <psp2/kernel/sysmem.h>",
        "#include <psp2/types.h>",
        //"#include <psp2/moduleinfo.h>",
        "#include <psp2/kernel/processmgr.h>",
        //"#include <vita2d.h>",
        "}",
        "#define SCREEN_W 960",
        "#define SCREEN_H 544",
        "#define align_mem(addr, align) (((addr) + ((align) - 1)) & ~((align) - 1))",
        "static SceDisplayFrameBuf fb[2];",
        "static int cur_fb = 0;",
        "static SceCtrlData pad = {0};",
        "static SceTouchData touch = {0};",
})
@JTranscAddDefines(target = "cpp", value = "DISABLE_BOEHM_GC")
@JTranscAddLibraries(target = "cpp", value = {"pthread"})
public class VitaSdk {
    static public void init() {

    }
}
