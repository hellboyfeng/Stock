#! /usr/bin/env python
# -*- coding: utf-8 -*-
import tushare as ts
df = ts.get_stock_basics()
df.to_csv('d:/stock.csv')
