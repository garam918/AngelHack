package com.example.garam.angelhack.network

data class Post(
    var cid: String,
    var partner_order_id: String,
    var partner_user_id: String,
    var item_name: String,
    var quantity: Int,
    var total_amount: Int,
    var tax_free_amount: Int,
    var approval_url: String,
    var fail_url: String,
    var cancel_url: String
)