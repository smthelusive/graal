/*
 * Copyright (c) 2022, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.svm.core.riscv64;

import jdk.graal.compiler.api.replacements.Fold;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;
import org.graalvm.nativeimage.c.function.CodePointer;
import org.graalvm.word.Pointer;

import com.oracle.svm.core.FrameAccess;
import com.oracle.svm.core.Uninterruptible;
import com.oracle.svm.core.feature.AutomaticallyRegisteredImageSingleton;

@AutomaticallyRegisteredImageSingleton(FrameAccess.class)
@Platforms(Platform.RISCV64.class)
public class RISCV64FrameAccess extends FrameAccess {
    @Override
    @Uninterruptible(reason = "Called from uninterruptible code.", mayBeInlined = true)
    public CodePointer readReturnAddress(Pointer sourceSp) {
        /* Read the return address, which is stored immediately below the stack pointer */
        return sourceSp.readWord(-returnAddressSize());
    }

    @Override
    @Uninterruptible(reason = "Called from uninterruptible code.", mayBeInlined = true)
    public void writeReturnAddress(Pointer sourceSp, CodePointer newReturnAddress) {
        sourceSp.writeWord(-returnAddressSize(), newReturnAddress);
    }

    @Override
    @Uninterruptible(reason = "Called from uninterruptible code.", mayBeInlined = true)
    public Pointer getReturnAddressLocation(Pointer sourceSp) {
        return sourceSp.subtract(returnAddressSize());
    }

    @Fold
    @Override
    public int savedBasePointerSize() {
        // The base pointer is always pushed on the stack on method entry
        return wordSize();
    }

    @Override
    @Fold
    public int stackPointerAdjustmentOnCall() {
        // A call on RISCV64 does not touch the SP.
        return 0;
    }
}
