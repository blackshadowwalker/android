package com.cxf.net.handler;


import java.io.IOException;
import java.io.RandomAccessFile;

/** Represents the C WAVEFORMATEX structure. 
 *  http://msdn2.microsoft.com/en-us/library/ms713497(VS.85).aspx */
class WAVEFORMATEX extends AVIstruct {

    static short WAVE_FORMAT_PCM = 1;
    
    // ** VirtualDub writes 16 bytes struct (leaves off cbSize)
    
    public /*WORD */ short wFormatTag      = 0;
    public /*WORD */ short nChannels       = 0;
    public /*DWORD*/ int   nSamplesPerSec  = 0;
    public /*DWORD*/ int   nAvgBytesPerSec = 0;
    public /*WORD */ short nBlockAlign     = 0;
    public /*WORD */ short wBitsPerSample  = 0;
    //public /*WORD */ short cbSize          = 0; **
    
    
    public void write(RandomAccessFile raf) throws IOException {
        /*WORD */ write16LE(raf, wFormatTag     );
        /*WORD */ write16LE(raf, nChannels      );
        /*DWORD*/ write32LE(raf, nSamplesPerSec );
        /*DWORD*/ write32LE(raf, nAvgBytesPerSec);
        /*WORD */ write16LE(raf, nBlockAlign    );
        /*WORD */ write16LE(raf, wBitsPerSample );
        ///*WORD */ write16LE(raf, cbSize         ); **
    }

    public int sizeof() {
        return 18-2; // **
    }
    
}
