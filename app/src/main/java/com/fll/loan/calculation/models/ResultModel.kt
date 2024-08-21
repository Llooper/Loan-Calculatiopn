package com.fll.loan.calculation.models

import androidx.lifecycle.ViewModel
import com.fll.loan.calculation.ui.CalculationScreen
import com.fll.loan.calculation.ui.ResultItem
import com.fll.loan.calculation.ui.ResultTitle
import com.fll.loan.calculation.ui.common.ResultItemLayoutType
import com.fll.loan.calculation.ui.common.format2
import java.math.BigDecimal
import java.math.RoundingMode

class ResultModel : ViewModel() {

    private var valueTips = ""
    fun setValueTips(tips: String) {
            this.valueTips = tips
    }

    fun calculate(loanBean: LoanBean): ArrayList<ResultItem>? {
        if ((loanBean.amount > 0.0f) and (loanBean.fundAmount == 0.0f)) {
            return calculateCommercialResult(loanBean)
        }
        if ((loanBean.amount == 0.0f) and (loanBean.fundAmount > 0.0f)) {
            return calculateFundResult(loanBean)
        }

        if ((loanBean.amount > 0.0f) and (loanBean.fundAmount > 0.0f)) {
            val tmpLists = ArrayList<ResultItem>()
            tmpLists.addAll(
                calculateCommercialResult(
                    loanBean,
                    titleTips = CalculationScreen.CommercialLoan.title
                )
            )
            tmpLists.addAll(
                tmpLists.size,
                calculateFundResult(loanBean, titleTips = CalculationScreen.ProvidentFundLoan.title)
            )
            return tmpLists
        }
        return null
    }

    private fun calculateCommercialResult(
        loanBean: LoanBean,
        titleTips: Int? = null
    ): ArrayList<ResultItem> {
        return calculateResult(loanBean.amount, loanBean.rate, loanBean.yearCount, titleTips)
    }

    private fun calculateFundResult(
        loanBean: LoanBean,
        titleTips: Int? = null
    ): ArrayList<ResultItem> {
        return calculateResult(
            loanBean.fundAmount,
            loanBean.fundRate,
            loanBean.yearCount,
            titleTips
        )
    }

    private fun calculateResult(
        amount: Float,
        rate: Float,
        year: Int,
        tips: Int? = null
    ): ArrayList<ResultItem> {
        val resultItemLists = ArrayList<ResultItem>()

        val rateRatio = BigDecimal(100)
        val moneyRatio = BigDecimal(10000)
        val monthRatio = BigDecimal(12)
        val foundRatio = BigDecimal(1)
        val towNumber = BigDecimal(2)

        // 获取月利率
        var yearRateNumber = BigDecimal(rate.toDouble())
        yearRateNumber = yearRateNumber.divide(rateRatio)
        val monthRateNumber = yearRateNumber.divide(monthRatio, 18, RoundingMode.CEILING)

        // 获取还款月数
        val monthCount = year * 12
        val yearCountNumber = BigDecimal(year)
        val monthCountNumber = yearCountNumber.multiply(monthRatio)

        // 贷款总金额
        var loanMoneyNumber = BigDecimal(amount.toDouble())
        loanMoneyNumber = loanMoneyNumber.multiply(moneyRatio)

        // 等额本息计算公式：〔贷款本金×月利率×（1＋月利率）＾还款月数〕÷〔（1＋月利率）＾还款月数－1〕
        // 1.贷款本金×月利率
        val step1Number = loanMoneyNumber.multiply(monthRateNumber)
        // 2.（1＋月利率）
        val step2Number = monthRateNumber.add(foundRatio)
        // 3.（1＋月利率）＾还款月数
        val step3Number = step2Number.pow(monthCount)
        // 4.〔（1＋月利率）＾还款月数－1〕
        val step4Number = step3Number.subtract(foundRatio)
        // 5.〔贷款本金×月利率×（1＋月利率）＾还款月数〕÷〔（1＋月利率）＾还款月数－1〕
        val bxMonthPayMoneyNumber =
            step1Number.multiply(step3Number).divide(step4Number, 18, RoundingMode.CEILING)

        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.HeaderTxt,
                itemTitle = ResultTitle.BasicInformation.itemTitle,
                titleTips = tips
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.LoanAmount.itemTitle,
                itemValue = amount.format2().toString()
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.YearCount.itemTitle,
                itemValue = "%d(%d %s)".format(year, monthCountNumber.toBigInteger(), valueTips)
            )
        )

        val bxMPayMoney = bxMonthPayMoneyNumber.toDouble()
        val totalPayMoney = bxMPayMoney * monthCount / 10000
        val totalInterestMoney = totalPayMoney - amount

        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.HeaderTxt,
                itemTitle = ResultTitle.EqualInterest.itemTitle,
                titleTips = tips
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.MonthlyRepayment.itemTitle,
                itemValue = bxMPayMoney.toFloat().format2().toString()
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.TotalRepayment.itemTitle,
                itemValue = totalPayMoney.toFloat().format2().toString()
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.TotalInterest.itemTitle,
                itemValue = totalInterestMoney.toFloat().format2().toString()
            )
        )

        // 等额本金计算公式：每月还款金额 = （贷款本金 / 还款月数）+（本金 — （（贷款本金 / 还款月数 ）* 第几期）×每月利率 。
        // 1.（贷款本金 / 还款月数）
        val cap1Number = loanMoneyNumber.divide(monthCountNumber, 18, RoundingMode.CEILING)
        // 3.本金 × 每月利率
        val cap3Number = loanMoneyNumber.multiply(monthRateNumber)
        // 首月还款
        val capFirstMonthPayMoneyNumber = cap1Number.add(cap3Number)
        // 每月递减
        val decreaseMoneyNumber = cap1Number.multiply(monthRateNumber)
        // 还款总利息
        var totalInterestNumber = loanMoneyNumber.multiply(monthRateNumber)
        totalInterestNumber = totalInterestNumber.multiply(monthCountNumber.add(foundRatio))
        totalInterestNumber = totalInterestNumber.divide(towNumber, 18, RoundingMode.CEILING)

        val capPerMonthDecMoney = decreaseMoneyNumber.toDouble()
        val capResultMoney = capFirstMonthPayMoneyNumber.toDouble()
        val capTotalInterestMoney = totalInterestNumber.toDouble() / 10000.0
        val capTotalPayMoney = capTotalInterestMoney + amount

        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.HeaderTxt,
                itemTitle = ResultTitle.EqualPrincipal.itemTitle,
                titleTips = tips
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.FirstMonthRepayment.itemTitle,
                itemValue = capResultMoney.toFloat().format2().toString()
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.MonthlyDecrease.itemTitle,
                itemValue = capPerMonthDecMoney.toFloat().format2().toString()
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.TotalRepayment.itemTitle,
                itemValue = capTotalPayMoney.toFloat().format2().toString()
            )
        )
        resultItemLists.add(
            ResultItem(
                itemType = ResultItemLayoutType.ResultTxt,
                itemTitle = ResultTitle.TotalInterest.itemTitle,
                itemValue = capTotalInterestMoney.toFloat().format2().toString()
            )
        )

        return resultItemLists
    }
}