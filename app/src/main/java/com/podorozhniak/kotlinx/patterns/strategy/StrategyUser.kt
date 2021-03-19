package com.podorozhniak.kotlinx.patterns.strategy

import com.podorozhniak.kotlinx.patterns.strategy.strategy.Strategy

data class StrategyUser(
    var strategy: Strategy
)