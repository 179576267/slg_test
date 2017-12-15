package com.douqu.game.close;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteBuffer implements Cloneable {
    private int a;
    private int b;
    private byte[] bytes1;
    private byte[] bytes2;

    public ByteBuffer() {
        this(1024);
    }

    public ByteBuffer(int i) {
        this.bytes2 = new byte[8];
        i += 8;
        this.bytes1 = new byte[i];
    }

    public ByteBuffer(byte[] i) {
        this(i, 0, i.length);
    }

    public ByteBuffer(byte[] i, int j, int k) {
        this.bytes2 = new byte[8];
        this.bytes1 = i;
        this.a = j;
        this.b = j + k;
    }

    public void writeBoolean(boolean i) {
        this.writeByte(i?1:0);
    }

    public void writeByte(int i) {
        this.bytes2((long)i, 1);
    }

    public void writeBytes(byte[] i) {
        this.writeBytes(i, 0, i.length);
    }

    public void writeBytes(byte[] i, int j, int k) {
        this.bytes1(this.b + k);

        for(int l = 0; l < k; ++l) {
            this.bytes1[this.b++] = i[j++];
        }

    }

    public void writeShort(int i) {
        this.bytes2((long)i, 2);
    }

    public void writeChar(char i) {
        this.bytes2((long)i, 2);
    }

    public void writeInt(int i) {
        this.bytes2((long)i, 4);
    }

    public void writeLong(long i) {
        this.bytes2(i, 8);
    }

    public void writeString(String i) {
        if(i != null && i.length() != 0) {
            if(i.length() > 32767) {
                throw new IllegalArgumentException("short string over flow");
            }

            int j = i.length();
            this.writeShort(j);

            for(int k = 0; k < j; ++k) {
                this.writeChar(i.charAt(k));
            }
        } else {
            this.writeShort(0);
        }

    }

    public void writeShortString(String i) {
        if(i != null && i.length() != 0) {
            if(i.length() > 255) {
                throw new IllegalArgumentException("short string over flow");
            }

            int j = i.length();
            this.writeByte(j);

            for(int k = 0; k < j; ++k) {
                this.writeChar(i.charAt(k));
            }
        } else {
            this.writeByte(0);
        }

    }

    public void writeUTF(String i) {
        if(i == null) {
            i = "";
        }

        int j = i.length();
        int k = 0;

        int l;
        char p;
        for(l = 0; l < j; ++l) {
            p = i.charAt(l);
            if(p < 127) {
                ++k;
            } else if(p > 2047) {
                k += 3;
            } else {
                k += 2;
            }
        }

        if(k > '\uffff') {
            throw new IllegalArgumentException("the string is too long:" + j);
        } else {
            this.bytes1(k + 2 + this.b);
            this.writeShort(k);

            for(l = 0; l < j; ++l) {
                p = i.charAt(l);
                if(p < 127) {
                    this.bytes1[this.b++] = (byte)p;
                } else if(p > 2047) {
                    this.bytes1[this.b++] = (byte)(224 | p >> 12 & 15);
                    this.bytes1[this.b++] = (byte)(128 | p >> 6 & 63);
                    this.bytes1[this.b++] = (byte)(128 | p >> 0 & 63);
                } else {
                    this.bytes1[this.b++] = (byte)(192 | p >> 6 & 31);
                    this.bytes1[this.b++] = (byte)(128 | p >> 0 & 63);
                }
            }

        }
    }

    public void writeByteBuffer(ByteBuffer i) {
        this.writeByteBuffer(i, i.available());
    }

    public void writeByteBuffer(ByteBuffer i, int j) {
        j = Math.min(j, i.available());
        this.bytes1(this.length() + j);

        for(int k = 0; k < j; ++k) {
            this.bytes1[this.b++] = i.bytes1[i.a++];
        }

    }

    public boolean readBoolean() {
        return this.readByte() != 0;
    }

    public int readByte() {
        return this.bytes1[this.a++];
    }

    public int readUnsignedByte() {
        return this.bytes1[this.a++] & 255;
    }

    public byte[] readBytes(int i) {
        byte[] j = new byte[i];

        for(int k = 0; k < i; ++k) {
            j[k] = this.bytes1[this.a++];
        }

        return j;
    }

    public int readShort() {
        return (short)((int)(this.bytes2(2) & 65535L));
    }

    public char readChar() {
        return (char)((int)(this.bytes2(2) & 65535L));
    }

    public int readUnsignedShort() {
        return (int)(this.bytes2(2) & 65535L);
    }

    public int readInt() {
        return (int)(this.bytes2(4) & -1L);
    }

    public long readLong() {
        return this.bytes2(8);
    }

    public String readShortString() {
        int i = this.readUnsignedByte();
        if(i == 0) {
            return "";
        } else {
            StringBuffer j = new StringBuffer(i);

            for(int k = 0; k < i; ++k) {
                j.append(this.readChar());
            }

            return j.toString();
        }
    }

    public String readString() {
        int i = this.readUnsignedShort();
        if(i == 0) {
            return "";
        } else {
            StringBuffer j = new StringBuffer(i);

            for(int k = 0; k < i; ++k) {
                j.append(this.readChar());
            }

            return j.toString();
        }
    }

    public String readUTF() {
        int i = this.readUnsignedShort();
        if(i == 0) {
            return "";
        } else {
            char[] j = new char[i];
            int k = 0;
            int q = this.a + i;

            while(this.a < q) {
                int w = this.bytes1[this.a++] & 255;
                if(w < 127) {
                    j[k++] = (char)w;
                } else {
                    byte e;
                    if(w >> 5 == 7) {
                        e = this.bytes1[this.a++];
                        byte i0 = this.bytes1[this.a++];
                        j[k++] = (char)((w & 15) << 12 | (e & 63) << 6 | i0 & 63);
                    } else {
                        e = this.bytes1[this.a++];
                        j[k++] = (char)((w & 31) << 6 | e & 63);
                    }
                }
            }

            return new String(j, 0, k);
        }
    }

    public void readFrom(InputStream i) throws IOException {
        this.readFrom(i, this.capacity() - this.length());
    }

    public void readFrom(InputStream i, int j) throws IOException {
        this.bytes1(this.b + j);

        for(int k = 0; k < j; ++k) {
            this.bytes1[this.b++] = (byte)i.read();
        }

    }

    public void writeTo(OutputStream i) throws IOException {
        int j = this.available();

        for(int k = 0; k < j; ++k) {
            i.write(this.bytes1[this.a++]);
        }

    }

    public int capacity() {
        return this.bytes1.length;
    }

    public int length() {
        return this.b;
    }

    public int position() {
        return this.a;
    }

    public void position(int i) {
        if(i >= 0 && i <= this.b) {
            this.a = i;
        } else {
            throw new IllegalArgumentException("position out of range:" + i);
        }
    }

    public byte[] getBytes() {
        byte[] i = new byte[this.length()];
        System.arraycopy(this.bytes1, 0, i, 0, i.length);
        return i;
    }

    public byte[] getRawBytes() {
        return this.bytes1;
    }

    public int available() {
        return this.b - this.a;
    }

    public void clear() {
        this.b = this.a = 0;
    }

    public void reset() {
        this.a = 0;
    }

    public void pack() {
        if(this.a != 0) {
            int i = this.available();

            for(int j = 0; j < i; ++j) {
                this.bytes1[j] = this.bytes1[this.a++];
            }

            this.a = 0;
            this.b = i;
        }
    }

    public String toString() {
        return new String(this.bytes1, 0, this.b);
    }

    public Object clone() {
        ByteBuffer i = new ByteBuffer(this.b);
        System.arraycopy(this.bytes1, 0, i.bytes1, 0, this.b);
        i.b = this.b;
        i.a = this.a;
        return i;
    }

    private void bytes1(byte[] i, int j, int k, int l) {
        this.bytes1(l + k);
        System.arraycopy(i, j, this.bytes1, l, k);
    }


    private void bytes1(int i) {
        if(i > this.bytes1.length) {
            byte[] j = new byte[i * 3 / 2];
            System.arraycopy(this.bytes1, 0, j, 0, this.b);
            this.bytes1 = j;
        }

    }

    private void bytes2(long i, int k) {
        for(int l = 0; l < k; ++l) {
            this.bytes2[l] = (byte)((int)(i >> l * 8));
        }

        this.bytes1(this.bytes2, 0, k, this.b);
        this.b += k;
    }

    private long bytes2(int i) {
        for(int j = 0; j < i; ++j) {
            this.bytes2[j] = this.bytes1[this.a++];
        }

        long p = 0L;

        for(int l = 0; l < i; ++l) {
            p |= (long)(this.bytes2[l] & 255) << l * 8;
        }

        return p;
    }
}
