package com.equalinfotech.coffee.api

import com.eaxample.coffee.api.APIConfiguration


class APIUtils {

    companion object {

        private val BASE_URL = "http://dev.equalinfotech.in/coffeecup/ConsumerApi/"

        fun getServiceAPI(): APIConfiguration? {
            return APIClient.getApiClient(BASE_URL)!!.create(APIConfiguration::class.java)

        }
    }

}