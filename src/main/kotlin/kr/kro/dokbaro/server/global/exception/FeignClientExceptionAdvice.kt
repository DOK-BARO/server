package kr.kro.dokbaro.server.global.exception

import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class FeignClientExceptionAdvice {
	@ExceptionHandler(FeignException::class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	fun badRequest(e: FeignException): Nothing = throw FeignClientBadRequestException()
}