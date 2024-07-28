package kr.kro.dokbaro.server.global.exception.http

import kr.kro.dokbaro.server.global.exception.http.status4xx.BadRequestException
import kr.kro.dokbaro.server.global.exception.http.status4xx.ForbiddenException
import kr.kro.dokbaro.server.global.exception.http.status4xx.NotFoundException
import kr.kro.dokbaro.server.global.exception.http.status4xx.UnauthorizedException
import kr.kro.dokbaro.server.global.exception.http.status5xx.InternalServerException
import kr.kro.dokbaro.server.global.exception.http.status5xx.NotImplementedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionControllerAdvice {
	@ExceptionHandler(BadRequestException::class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	fun badRequest(e: BadRequestException) = ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message)

	@ExceptionHandler(ForbiddenException::class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	fun forbidden(e: ForbiddenException) = ErrorResponse(HttpStatus.FORBIDDEN.value(), e.message)

	@ExceptionHandler(NotFoundException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun notFound(e: NotFoundException) = ErrorResponse(HttpStatus.NOT_FOUND.value(), e.message)

	@ExceptionHandler(UnauthorizedException::class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	fun unauthorized(e: UnauthorizedException) = ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.message)

	@ExceptionHandler(InternalServerException::class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	fun internalServerError(e: InternalServerException) =
		ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)

	@ExceptionHandler(NotImplementedException::class)
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	fun notImplemented(e: NotImplementedException) = ErrorResponse(HttpStatus.NOT_IMPLEMENTED.value(), e.message)

	@ExceptionHandler(RuntimeException::class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	fun runtimeException(e: RuntimeException) = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message)
}