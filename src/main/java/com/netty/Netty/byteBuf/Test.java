package com.netty.Netty.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

public class Test {
    public static void main(String[] args) {
        // 容量设置，会随字节大小而变化
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        // 此外，Netty自带的还有相关的池化技术，通过这些池化
    }
}
