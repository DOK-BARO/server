package kr.kro.dokbaro.server.core.account.application.service

import org.springframework.stereotype.Component

/**
 * 임시 비밀번호를 생성합니다.
 */
@Component
class TemporaryPasswordGenerator {
	/**
	 * 영어 대소문자 및 숫자를 혼합하여 9글자의 문자열을 생성합니다.
	 */
	fun generate(): String {
		val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

		return (1..9).map { chars.random() }.joinToString("")
	}
}