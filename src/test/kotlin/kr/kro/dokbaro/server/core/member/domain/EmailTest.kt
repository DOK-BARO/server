package kr.kro.dokbaro.server.core.member.domain

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class EmailTest :
	StringSpec({

		"이메일 형식이 정상적이면 객체를 정상적으로 생성한다" {
			shouldNotThrow<InvalidEmailFormatException> {
				Email("aaa@example.com")
			}
		}

		"이메일 형식이 맞지 않으면 에러를 반환한다" {
			listOf("aaa", "@asdf.com", "dasf@")
				.forEach {
					shouldThrow<InvalidEmailFormatException> {
						Email(it)
					}
				}
		}
	})