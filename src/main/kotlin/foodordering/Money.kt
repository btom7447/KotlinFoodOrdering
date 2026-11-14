/*
 * Formatting for naira amounts, so every price in the app looks the same.
 */
package foodordering

import java.util.Locale

private const val NAIRA_SIGN = "₦"

/**
 * Formats a whole-naira amount with the naira sign and thousands separators, for example `₦4,500`.
 *
 * `Locale.US` is fixed so the separator is always a comma, whatever the computer's region setting.
 *
 * @return the formatted amount.
 */
fun formatNaira(amount: Int): String = NAIRA_SIGN + String.format(Locale.US, "%,d", amount)
