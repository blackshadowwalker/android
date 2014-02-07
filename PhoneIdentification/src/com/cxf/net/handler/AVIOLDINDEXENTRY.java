package com.cxf.net.handler;

import java.io.IOException;
import java.io.RandomAccessFile;



public  class AVIOLDINDEXENTRY extends AVIstruct {
    public /*DWORD*/ int dwChunkId = 0;
    public /*DWORD*/ int dwFlags   = 0;
    public /*DWORD*/ int dwOffset  = 0;
    public /*DWORD*/ int dwSize    = 0;
    
    @Override
    public void write(RandomAccessFile raf) throws IOException {
        write32LE(raf, dwChunkId);
        write32LE(raf, dwFlags  );
        write32LE(raf, dwOffset );
        write32LE(raf, dwSize   );
    }

    @Override
    public int sizeof() {
        return 16;
    }
    
    public static AVIOLDINDEXENTRY[] newarray(int i) {
        AVIOLDINDEXENTRY[] a = new AVIOLDINDEXENTRY[i];
        for (int j = 0; j < a.length; j++) {
            a[j] = new AVIOLDINDEXENTRY();
        }
        return a;
    }
    
}