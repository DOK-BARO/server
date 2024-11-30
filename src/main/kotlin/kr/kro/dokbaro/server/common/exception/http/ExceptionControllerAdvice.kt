package kr.kro.dokbaro.server.common.exception.http

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException
import kr.kro.dokbaro.server.common.exception.http.status4xx.ForbiddenException
import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException
import kr.kro.dokbaro.server.common.exception.http.status4xx.UnauthorizedException
import kr.kro.dokbaro.server.common.exception.http.status5xx.InternalServerException
import kr.kro.dokbaro.server.common.exception.http.status5xx.NotImplementedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {
	@ExceptionHandler(BadRequestException::class)
	fun badRequest(e: BadRequestException) =
		ResponseEntity(
			ErrorResponse(
				status = HttpStatus.BAD_REQUEST.value(),
				message = e.message,
			),
			HttpStatus.BAD_REQUEST,
		)

	@ExceptionHandler(ForbiddenException::class)
	fun forbidden(e: ForbiddenException) =
		ResponseEntity(
			ErrorResponse(
				status = HttpStatus.FORBIDDEN.value(),
				message = e.message,
			),
			HttpStatus.FORBIDDEN,
		)

	@ExceptionHandler(NotFoundException::class)
	fun notFound(e: NotFoundException) =
		ResponseEntity(
			ErrorResponse(
				status = HttpStatus.NOT_FOUND.value(),
				message = e.message,
			),
			HttpStatus.NOT_FOUND,
		)

	@ExceptionHandler(UnauthorizedException::class)
	fun unauthorized(e: UnauthorizedException) =
		ResponseEntity(
			ErrorResponse(
				status = HttpStatus.UNAUTHORIZED.value(),
				message = e.message,
			),
			HttpStatus.UNAUTHORIZED,
		)

	@ExceptionHandler(InternalServerException::class)
	fun internalServerError(e: InternalServerException) =
		ResponseEntity(
			ErrorResponse(
				status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
				message = e.message,
			),
			HttpStatus.INTERNAL_SERVER_ERROR,
		)

	@ExceptionHandler(NotImplementedException::class)
	fun notImplemented(e: NotImplementedException) =
		ResponseEntity(
			ErrorResponse(
				status = HttpStatus.NOT_IMPLEMENTED.value(),
				message = e.message,
			),
			HttpStatus.NOT_IMPLEMENTED,
		)

	@ExceptionHandler(RuntimeException::class)
	fun runtimeException(e: RuntimeException) =
		ResponseEntity(
			ErrorResponse(
				status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
				message = e.message,
			),
			HttpStatus.INTERNAL_SERVER_ERROR,
		)
}