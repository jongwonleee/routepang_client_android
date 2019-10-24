package com.itaewonproject

import com.kakao.auth.*

class KakaoSDKAdapter : KakaoAdapter() {
    override fun getApplicationConfig(): IApplicationConfig {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {
            override fun isSaveFormData(): Boolean {
                return true
            }

            override fun isSecureMode(): Boolean {
                return true
            }

            override fun getApprovalType(): ApprovalType? {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isUsingWebviewTimer(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAuthTypes(): Array<AuthType> {
                return arrayOf(AuthType.KAKAO_ACCOUNT)

            }
        }
    }

}