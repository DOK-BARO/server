package kr.kro.dokbaro.server.security.jwt.impl

import kr.kro.dokbaro.server.security.jwt.JwtResponseHandler
import org.springframework.stereotype.Component

@Component
class JwtCookieResponseStrategy : JwtResponseHandler