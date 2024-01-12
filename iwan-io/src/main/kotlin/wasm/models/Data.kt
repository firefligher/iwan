package dev.fir3.iwan.io.wasm.models

sealed interface Data

data class Data0(val e: Expression, val b: List<Byte>): Data
data class Data1(val b: List<Byte>): Data
data class Data2(val x: UInt, val e: Expression, val b: List<Byte>): Data
