package com.example.wemahostels.models

import android.content.ClipDescription
import androidx.compose.ui.graphics.PathSegment
import com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location

data class House(


    var imageUrl: String="",
    var houseName:String="",
    var amenities:String="",
    var houseType:String="",
    var description:String="",
    var price:String="",
    var fee:String="",
    var location:String="",
    var id:String=""
)
