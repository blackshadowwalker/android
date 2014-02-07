package com.cxf.net.handler;


import java.io.IOException;
import java.io.RandomAccessFile;

/** Represents the C BITMAPINFOHEADER structure. 
 * http://msdn2.microsoft.com/en-us/library/ms532290(VS.85).aspx */
class BITMAPINFOHEADER extends AVIstruct {
    
    public final static int BI_RGB = 0;

    public final /*DWORD*/ int   biSize           = sizeof();
    public       /*LONG */ int   biWidth          = 0;
    public       /*LONG */ int   biHeight         = 0;
    public final /*WORD */ short biPlanes         = 1;
    public       /*WORD */ short biBitCount       = 0;
    public       /*DWORD*/ int   biCompression    = 0;
    public       /*DWORD*/ int   biSizeImage      = 0;
    public       /*LONG */ int   biXPelsPerMeter  = 0;
    public       /*LONG */ int   biYPelsPerMeter  = 0;
    public       /*DWORD*/ int   biClrUsed        = 0;
    public       /*DWORD*/ int   biClrImportant   = 0;

    @Override
    public void write(RandomAccessFile raf) throws IOException {
        /*DWORD*/ write32LE(raf, biSize         );
        /*LONG */ write32LE(raf, biWidth        );
        /*LONG */ write32LE(raf, biHeight       );
        /*WORD */ write16LE(raf, biPlanes       );
        /*WORD */ write16LE(raf, biBitCount     );
        /*DWORD*/ write32LE(raf, biCompression  );
        /*DWORD*/ write32LE(raf, biSizeImage    );
        /*LONG */ write32LE(raf, biXPelsPerMeter);
        /*LONG */ write32LE(raf, biYPelsPerMeter);
        /*DWORD*/ write32LE(raf, biClrUsed      );
        /*DWORD*/ write32LE(raf, biClrImportant );
    }

    @Override
    public int sizeof() {
        return 40;
    }
    
}
