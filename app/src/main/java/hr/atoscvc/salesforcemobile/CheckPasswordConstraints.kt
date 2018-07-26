package hr.atoscvc.salesforcemobile

object CheckPasswordConstraints {
    fun checkPasswordConstraints(user: String, pass: String): RegisterActivity.PasswordErrors {
        if (pass.length < 8) {
            return RegisterActivity.PasswordErrors("Password must be at least 8 characters long", false)
        }
        if (pass.length > 72) {
            return RegisterActivity.PasswordErrors("Password cannot contain more than 72 characters", false)
        }
        if (user.isNotEmpty() && pass.contains(user, true)) {
            return RegisterActivity.PasswordErrors("Password must not contain the username", false)
        }
        if (user.contains(pass, true)) {
            return RegisterActivity.PasswordErrors("Username must not contain the password", false)
        }
        if (!pass.matches(".*[A-Z].*".toRegex())) {
            return RegisterActivity.PasswordErrors("Password must contain at least one uppercase letter", false)
        }
        if (!pass.matches(".*[a-z].*".toRegex())) {
            return RegisterActivity.PasswordErrors("Password must contain at least one lowercase letter", false)
        }
        if (!pass.matches(".*\\d.*".toRegex())) {
            return RegisterActivity.PasswordErrors("Password must contain at least one digit", false)
        }

        return RegisterActivity.PasswordErrors("Password is fine.", true)
    }
}