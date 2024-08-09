package kr.kro.dokbaro.server.core

import kr.kro.dokbaro.server.common.dto.response.MessageResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health-check")
class HealthController {
	@GetMapping
	fun getHealth() = MessageResponse("OK")

	@PostMapping
	fun postHealth() = MessageResponse("OK")
}