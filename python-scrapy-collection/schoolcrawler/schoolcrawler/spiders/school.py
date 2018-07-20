# -*- coding: utf-8 -*-
import scrapy
import re
from schoolcrawler.items import SchoolcrawlerItem

class SchoolSpider(scrapy.Spider):
    name = "school"

    #http://xuexiao.51sxue.com/slist/?t=学校类型&areaCodeS=城市编号&level=学校属性
    #t=2 小学 t=3 中学 t=4 大学
    #level=1-5 重点 level=6 非重点
    start_urls = [
    'http://xuexiao.51sxue.com/slist/?t=2&areaCodeS=12&level=1&page=1',
    'http://xuexiao.51sxue.com/slist/?t=2&areaCodeS=12&level=2&page=1',
    'http://xuexiao.51sxue.com/slist/?t=2&areaCodeS=12&level=3&page=1',
    'http://xuexiao.51sxue.com/slist/?t=2&areaCodeS=12&level=4&page=1',
    'http://xuexiao.51sxue.com/slist/?t=2&areaCodeS=12&level=5&page=1',
    'http://xuexiao.51sxue.com/slist/?t=2&areaCodeS=12&level=6&page=1'
    ]

    def parse(self, response):
        domain="http://xuexiao.51sxue.com"

        item = SchoolcrawlerItem()
        print "Start"
        selector = scrapy.Selector(response)
        schools = selector.xpath('//div[@class="school_main"]/div[@id]')
        for school in schools:
            name = school.xpath('div[@class="school_t_con"]/div[@class="school_m_main fl"]/li[1]/h3/a/text()').extract()[0];
            print "name: "+ name.encode('utf-8')
            address = school.xpath('ul[@class="school_m_lx"]/li[@class="school_dz"]/b/text()').extract()[0];
            print "addr: "+address.encode('utf-8')
            params = response.url.split('&')
            level = params[len(params)-2]

            item['name'] = name
            item['addr'] = address

            print level
            if level == "level=6":
                item['key'] = "普通"
            else:
                item['key'] = "重点"
            yield item

            nextPage = ""
            downBtn = selector.xpath('//div[@class="school_main"]/div[@class="school_page"]/div[@class="page_w"][1]/div[1]/a[@class="down"]')
            for btn in downBtn:
                btnText = btn.xpath('text()').extract()[0]
                if btnText.encode('utf-8') == "下一页":
                    nextPage = btn.xpath('@href').extract()[0]
                    print nextPage
                    break

            if nextPage != "":
                next = domain + nextPage
                yield scrapy.http.Request(next, callback=self.parse)

            # downBtn = selector.xpath('//div[@class="school_main"]/div[@class="school_page"]/div[@class="page_w"][1]/div[1]/a[@class="down"]/text()').extract();
            #
            # idx
            # for btn in downBtn:
            #     print btn
            #     if btn.encode('utf-8') == "下一页":
            #         idx+=1
            #
            # print idx
            #
            #
            # nextPage = selector.xpath('//div[@class="school_main"]/div[@class="school_page"]/div[@class="page_w"][1]/div[1]/a[@class="down"]['+str(idx)+']/@href').extract()
            # if nextPage:
            #     next = domain + nextPage[0]
            #     print next
            #     yield scrapy.http.Request(next, callback=self.parse)



        print "End"
