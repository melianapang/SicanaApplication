package com.example.capstoneproject.onboarding.intro

object OnboardingObject {
    fun generateObject(): List<OnboardingModel>{
        val arrayList = ArrayList<OnboardingModel>()
        arrayList.add(
            OnboardingModel(
                0,
                "Selamat datang di SiCana"
            )
        )
        arrayList.add(
            OnboardingModel(
                1,
                "Mari kita sigap, tanggap, dan bantu sesama"
            )
        )
        arrayList.add(
            OnboardingModel(
                2,
                "Bagi informasi kamu melalui SiCana."
            )
        )
        return arrayList
    }
}