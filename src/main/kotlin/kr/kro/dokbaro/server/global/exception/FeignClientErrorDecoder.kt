package kr.kro.dokbaro.server.global.exception

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Configuration

@Configuration
class FeignClientErrorDecoder : ErrorDecoder {
	override fun decode(
		methodKey: String?,
		response: Response?,
	): Exception = throw FeignClientBadRequestException(response?.request()?.url())
}