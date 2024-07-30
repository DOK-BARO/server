package kr.kro.dokbaro.server.global.exception.http

import kr.kro.dokbaro.server.global.exception.http.status4xx.BadRequestException
import kr.kro.dokbaro.server.global.exception.http.status4xx.ForbiddenException
import kr.kro.dokbaro.server.global.exception.http.status4xx.NotFoundException
import kr.kro.dokbaro.server.global.exception.http.status4xx.UnauthorizedException
import kr.kro.dokbaro.server.global.exception.http.status5xx.InternalServerException
import kr.kro.dokbaro.server.global.exception.http.status5xx.NotImplementedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {
	@ExceptionHandler(BadRequestException::class)
	fun badRequest(e: BadRequestException) =
		ResponseEntity(
			ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message),
			HttpStatus.BAD_REQUEST,
		)

	@ExceptionHandler(ForbiddenException::class)
	fun forbidden(e: ForbiddenException) =
		ResponseEntity(
			ErrorResponse(HttpStatus.FORBIDDEN.value(), e.message),
			HttpStatus.FORBIDDEN,
		)

	@ExceptionHandler(NotFoundException::class)
	fun notFound(e: NotFoundException) =
		ResponseEntity(
			ErrorResponse(HttpStatus.NOT_FOUND.value(), e.message),
			HttpStatus.NOT_FOUND,
		)

	@ExceptionHandler(UnauthorizedException::class)
	fun unauthorized(e: UnauthorizedException) =
		ResponseEntity(
			ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.message),
			HttpStatus.UNAUTHORIZED,
		)

	@ExceptionHandler(InternalServerException::class)
	fun internalServerError(e: InternalServerException) =
		ResponseEntity(
			ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message),
			HttpStatus.INTERNAL_SERVER_ERROR,
		)

	@ExceptionHandler(NotImplementedException::class)
	fun notImplemented(e: NotImplementedException) =
		ResponseEntity(
			ErrorResponse(HttpStatus.NOT_IMPLEMENTED.value(), e.message),
			HttpStatus.NOT_IMPLEMENTED,
		)

	@ExceptionHandler(RuntimeException::class)
	fun runtimeException(e: RuntimeException) =
		ResponseEntity(
			ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message),
			HttpStatus.INTERNAL_SERVER_ERROR,
		)
}