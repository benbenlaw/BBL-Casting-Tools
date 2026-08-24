package com.benbenlaw.castingtools.utils;

import com.mojang.datafixers.util.Function13;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class BiggerStreamCodec {


    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12,
            final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13,
            final Function<C, T13> getter13,
            final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> constructor) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B input) {
                T1 t1 = codec1.decode(input);
                T2 t2 = codec2.decode(input);
                T3 t3 = codec3.decode(input);
                T4 t4 = codec4.decode(input);
                T5 t5 = codec5.decode(input);
                T6 t6 = codec6.decode(input);
                T7 t7 = codec7.decode(input);
                T8 t8 = codec8.decode(input);
                T9 t9 = codec9.decode(input);
                T10 t10 = codec10.decode(input);
                T11 t11 = codec11.decode(input);
                T12 t12 = codec12.decode(input);
                T13 t13 = codec13.decode(input);
                return constructor.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
            }

            @Override
            public void encode(B output, C value) {
                codec1.encode(output, getter1.apply(value));
                codec2.encode(output, getter2.apply(value));
                codec3.encode(output, getter3.apply(value));
                codec4.encode(output, getter4.apply(value));
                codec5.encode(output, getter5.apply(value));
                codec6.encode(output, getter6.apply(value));
                codec7.encode(output, getter7.apply(value));
                codec8.encode(output, getter8.apply(value));
                codec9.encode(output, getter9.apply(value));
                codec10.encode(output, getter10.apply(value));
                codec11.encode(output, getter11.apply(value));
                codec12.encode(output, getter12.apply(value));
                codec13.encode(output, getter13.apply(value));

            }
        };
    }
}
