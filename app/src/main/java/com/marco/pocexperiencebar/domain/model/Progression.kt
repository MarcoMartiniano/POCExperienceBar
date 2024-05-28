package com.marco.pocexperiencebar.domain.model


enum class Progression(val value: String, val level: Int, val cap: Float) {
    NEWBIE("Newbie", 1, 1000f),
    LEARNER("Learner", 2, 3000f),
    MASTER("Master", 3, 6000f),
    MAX("Max", 4, 6000f);

    companion object {
        fun getProgressByLevel(level: Int): Progression {
            return when (level) {
                1 -> NEWBIE
                2 -> LEARNER
                3 -> MASTER
                else -> MAX
            }
        }
    }
}