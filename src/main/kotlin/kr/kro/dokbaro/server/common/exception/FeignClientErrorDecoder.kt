package kr.kro.dokbaro.server.common.exception

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Configuration

/**
 * feignClient 사용 중 error 발생 시 자사 서비스 에러로 변환합니다.
 */
@Configuration
class FeignClientErrorDecoder : ErrorDecoder {
	override fun decode(
		methodKey: String?,
		response: Response?,
	): Exception = throw FeignClientBadRequestException(url = response?.request()?.url())
}