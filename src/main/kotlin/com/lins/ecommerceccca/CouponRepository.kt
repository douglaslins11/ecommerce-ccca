package com.lins.ecommerceccca

interface CouponRepository {
    fun getCoupon (code: String) : Coupon
}
