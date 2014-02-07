package com.cxf.net.handler;


import java.io.IOException;
import java.io.RandomAccessFile;

/** Represents the C AVIMAINHEADER structure. 
 * http://msdn2.microsoft.com/en-us/library/ms779632(VS.85).aspx */
class AVIMAINHEADER extends AVIstruct {
    public final static int AVIF_HASINDEX      = 0x00000010;
    public final static int AVIF_ISINTERLEAVED = 0x00000100;

    public final /*FOURCC*/ int fcc                    = string2int("avih");  
    public final /*DWORD */ int cb                     = sizeof() - 8;
    public       /*DWORD */ int dwMicroSecPerFrame     = 0;
    public       /*DWORD */ int dwMaxBytesPerSec       = 0;
    public       /*DWORD */ int dwPaddingGranularity   = 0;
    public       /*DWORD */ int dwFlags                = 0;
    public       /*DWORD */ int dwTotalFrames          = 0;
    public       /*DWORD */ int dwInitialFrames        = 0;
    public       /*DWORD */ int dwStreams              = 0;
    public       /*DWORD */ int dwSuggestedBufferSize  = 0;
    public       /*DWORD */ int dwWidth                = 0;
    public       /*DWORD */ int dwHeight               = 0;
    public final /*DWORD */ int dwReserved1            = 0;
    public final /*DWORD */ int dwReserved2            = 0;
    public final /*DWORD */ int dwReserved3            = 0;
    public final /*DWORD */ int dwReserved4            = 0;
    
    @Override
    public void write(RandomAccessFile raf) throws IOException {
        write32LE(raf, fcc                  );  
        write32LE(raf, cb                   );
        write32LE(raf, dwMicroSecPerFrame   );
        write32LE(raf, dwMaxBytesPerSec     );
        write32LE(raf, dwPaddingGranularity );
        write32LE(raf, dwFlags              );
        write32LE(raf, dwTotalFrames        );
        write32LE(raf, dwInitialFrames      );
        write32LE(raf, dwStreams            );
        write32LE(raf, dwSuggestedBufferSize);
        write32LE(raf, dwWidth              );
        write32LE(raf, dwHeight             );
        write32LE(raf, dwReserved1          );    
        write32LE(raf, dwReserved2          );    
        write32LE(raf, dwReserved3          );    
        write32LE(raf, dwReserved4          );    
    }
    
    @Override
    public int sizeof() {
        return 64;
    }
    
}
