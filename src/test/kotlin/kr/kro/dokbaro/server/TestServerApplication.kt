package kr.kro.dokbaro.server

import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
	fromApplication<ServerApplication>().with(TestcontainersConfiguration::class).run(*args)
}