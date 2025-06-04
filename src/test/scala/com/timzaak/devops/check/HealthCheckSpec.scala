package com.timzaak.devops.check

import munit.FunSuite

class HealthCheckSpec extends FunSuite {

  // --- HttpHealthCheck Tests ---

  test("HttpHealthCheck should return true for a valid and accessible URL (google.com)") {
    val healthCheck = new HttpHealthCheck("https://www.google.com")
    assertEquals(healthCheck.check(), true, "Google.com check failed")
  }

  test("HttpHealthCheck should return true for another valid HTTP URL (example.com)") {
    val healthCheck = new HttpHealthCheck("http://example.com")
    assertEquals(healthCheck.check(), true, "Example.com check failed")
  }

  test("HttpHealthCheck should return false for a malformed URL") {
    val healthCheck = new HttpHealthCheck("not_a_url")
    assertEquals(healthCheck.check(), false, "Malformed URL check did not return false")
  }

  test("HttpHealthCheck should return false for a URL that is likely to be inaccessible") {
    val healthCheck = new HttpHealthCheck("http://localhost:12345/nonexistent")
    // Assuming port 12345 is not typically open or serving this path
    assertEquals(healthCheck.check(), false, "Inaccessible URL check did not return false")
  }

  test("HttpHealthCheck should return false for an empty URL") {
    val healthCheck = new HttpHealthCheck("")
    assertEquals(healthCheck.check(), false, "Empty URL check did not return false")
  }
}
