package com.cxf.net.handler;


import java.io.IOException;
import java.io.RandomAccessFile;
        
/** Represents the C AVISTREAMHEADER structure. 
 * http://msdn2.microsoft.com/en-us/library/ms779638(VS.85).aspx
 * http://blog.jtimothyking.com/2006/12/15/does-bad-writing-reflect-poor-programming-skills
 */
class AVISTREAMHEADER extends AVIstruct {
     public final /*FOURCC*/ int   fcc                    = string2int("strh");
     public final /*DWORD */ int   cb                     = sizeof() - 8;
     public       /*FOURCC*/ int   fccType                = 0;
     public       /*FOURCC*/ int   fccHandler             = 0;
     public       /*DWORD */ int   dwFlags                = 0;
     public       /*WORD  */ short wPriority              = 0;
     public       /*WORD  */ short wLanguage              = 0;
     public       /*DWORD */ int   dwInitialFrames        = 0;
     public       /*DWORD */ int   dwScale                = 0;
     public       /*DWORD */ int   dwRate                 = 0;
     public       /*DWORD */ int   dwStart                = 0;
     public       /*DWORD */ int   dwLength               = 0;
     public       /*DWORD */ int   dwSuggestedBufferSize  = 0;
     public       /*DWORD */ int   dwQuality              = 0;
     public       /*DWORD */ int   dwSampleSize           = 0;
     //struct {
         public /*short int*/ short left            = 0;
         public /*short int*/ short top             = 0;
         public /*short int*/ short right           = 0;
         public /*short int*/ short bottom          = 0;
     //}  rcFrame    
    
    @Override
    public void write(RandomAccessFile raf) throws IOException {
        write32LE(raf, fcc);
        write32LE(raf, cb);
        write32LE(raf, fccType);
        write32LE(raf, fccHandler);
        write32LE(raf, dwFlags);
        write16LE(raf, wPriority);
        write16LE(raf, wLanguage);
        write32LE(raf, dwInitialFrames);
        write32LE(raf, dwScale);
        write32LE(raf, dwRate);
        write32LE(raf, dwStart);
        write32LE(raf, dwLength);
        write32LE(raf, dwSuggestedBufferSize);
        write32LE(raf, dwQuality);
        write32LE(raf, dwSampleSize);
        
        write16LE(raf, left);
        write16LE(raf, top);
        write16LE(raf, right);
        write16LE(raf, bottom); 
        
    }
    
    @Override
    public int sizeof() {
        return 64;
    }
    
}
