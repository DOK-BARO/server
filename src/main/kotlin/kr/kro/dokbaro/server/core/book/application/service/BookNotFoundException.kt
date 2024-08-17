package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class BookNotFoundException(
	val id: Long,
) : NotFoundException("Book($id) not found")