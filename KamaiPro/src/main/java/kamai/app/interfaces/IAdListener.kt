package kamai.app.interfaces

import kamai.app.enums.AdType

interface IAdListener {
    fun onAdResponse(ad: AdType)
}