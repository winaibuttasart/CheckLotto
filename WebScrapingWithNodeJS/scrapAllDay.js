var request = require('request');
var cheerio = require('cheerio');
const firebase = require('firebase');

let config = {
    apiKey: "apiKey ของคุณ",
    authDomain: "authDomain ของคุณ",
    databaseURL: "databaseURL ของคุณ",
    projectId: "projectId ของคุณ",
    storageBucket: "storageBucket ของคุณ",
    messagingSenderId: "messagingSenderId ของคุณ"
};

firebase.initializeApp(config);

var urlget = 'http://lottery.kapook.com/history.html';
var lotteryHistory = {};

request(urlget, function (error, response, html) {
    if (!error) {
        var a = cheerio.load(html);
        a('tbody').filter(function () {
            var data = a(this);
            var indata = data.children();
            var date, url;
            let len = Object.keys(indata).length;
            let i = 0;
            let exit = setInterval(function () {
                console.log('i : ', i);

                if (i < len) {
                    if (typeof (indata[i]) !== 'undefined') {
                        if (Object.keys(indata[i].children[1].children[0]).length === 5) {
                            if (typeof (indata[i].children[1].children[1]) !== 'undefined') {
                                date = indata[i].children[1].children[1].children[1].children[0].data;
                                url = indata[i].children[1].children[1].children[1].attribs.href;
                                console.log('date : ' + date);
                                console.log('url : ' + url);
                                changeDateFormat(date, url)
                            }

                        }
                    }
                    i++;
                } else {
                    console.log("Finish");
                    clearInterval(exit);
                }

            }, 5000);
        });
    }
});

function changeDateFormat(date, url) {
    var tmpURL = url.split('/');
    var fullLot = tmpURL[4].split('.')[0];
    var tmpFullLot = fullLot.split('-');
    var day = parseInt(tmpFullLot[2]);
    var month = parseInt(tmpFullLot[1]);
    var year = parseInt(tmpFullLot[0]) + 543;
    var dateTH = date.split(' ');
    var monthTH = dateTH[1];

    getData1(url, day, month, year, monthTH, date);
}

function getData1(url, day, month, year, monthTH, date) { //แกะวิธีแรก
    var lottery = {};
    lottery['date'] = {};
    lottery['first-prize'] = {};
    lottery['front3'] = {};
    lottery['back3'] = {};
    lottery['back2'] = {};
    lottery['nearby'] = {};
    lottery['second-prize'] = {};
    lottery['third-prize'] = {};
    lottery['four-prize'] = {};
    lottery['five-prize'] = {};
    var check = true;
    request(url, function (error, response, html) {
        if (!error) {
            var $ = cheerio.load(html);
            try {

                //วันที่
                $('h5').filter(function () {
                    var data = $(this);
                    var a = data.children();
                    lottery['date'] = {
                        'date': a[0].children[0].data
                    }
                });

                //รางวัลที่ 1
                $('.bigprize').filter(function () {
                    var data1 = $(this);
                    var a = data1.children();
                    lottery['first-prize'] = {
                        '1': a[0].children[1].children[0].data
                    }

                    lottery['front3'] = {
                        '1': a[1].children[3].children[1].children[0].data,
                        '2': a[1].children[3].children[3].children[0].data
                    }


                    lottery['back3'] = {
                        '1': a[1].children[5].children[1].children[0].data,
                        '2': a[1].children[5].children[3].children[0].data
                    }
                    lottery['back2'] = {
                        '1': a[2].children[2].children[0].data
                    }
                });

                //รางวัลใกล้เคียง => 2 รางวัล
                $('.nearby').filter(function () {
                    var data2 = $(this);
                    var b = data2.children();
                    lottery['nearby'] = {
                        '1': b[1].children[0].data,
                        '2': b[2].children[0].data
                    }
                });

                //รางวัลที่ 2  => 5 รางวัล
                $('.second-prize').filter(function () {
                    var data3 = $(this);
                    var c = data3.children();
                    for (var i = 1; i <= 5; i++) {
                        lottery['second-prize'][i] = c[i].children[0].data
                    }
                });

                //รางวัลที่ 3  => 10 รางวัล
                $('.third-prize').filter(function () {
                    var data4 = $(this);
                    var d = data4.children();
                    for (var i = 1; i <= 10; i++) {
                        lottery['third-prize'][i] = d[i].children[0].data
                    }
                });

                //รางวัลที่ 4  => 50 รางวัล
                $('.four-prize').filter(function () {
                    var data5 = $(this);
                    var e = data5.children();
                    for (var i = 1; i <= 50; i++) {
                        lottery['four-prize'][i] = e[i].children[0].data
                    }
                });

                //รางวัลที่ 5  => 100 รางวัล
                $('.five-prize').filter(function () {
                    var data6 = $(this);
                    var f = data6.children();
                    for (var i = 1; i <= 100; i++) {
                        lottery['five-prize'][i] = f[i].children[0].data
                    }
                });
            } catch (e) {
                getData2(url, day, month, year, monthTH, date);
                check = false;
            }
            if (check) {
                let ref = firebase.app().database().ref('/' + year + '/' + month + '/' + day);
                ref.set({
                    'numericDate': year + '/' + month + '/' + day,
                    'fullDate': date,
                    'shortYear': year,
                    'month': month,
                    'date': day,
                    'monthTH': monthTH,
                    'url': url,
                    'firstPrize': lottery['first-prize'],
                    'front3': lottery['front3'],
                    'back3': lottery['back3'],
                    'back2': lottery['back2'],
                    'nearby': lottery['nearby'],
                    'second-prize': lottery['second-prize'],
                    'third-prize': lottery['third-prize'],
                    'four-prize': lottery['four-prize'],
                    'five-prize': lottery['five-prize']
                });



                for (var j in lottery['first-prize']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['first-prize'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลที่ 1 งวดวันที่ : ' + date
                    });
                }

                for (var j in lottery['front3']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['front3'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลเลขหน้า 3 ตัว งวดวันที่ : ' + date
                    });
                }

                for (var j in lottery['back3']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['back3'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลเลขท้าย 3 ตัว งวดวันที่ : ' + date
                    });
                }

                for (var j in lottery['back2']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['back2'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลเลขท้าย 2 ตัว งวดวันที่ : ' + date
                    });
                }

                for (var j in lottery['nearby']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['nearby'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลรางวัลข้างเคียงรางวัลที่ 1 งวดวันที่ : ' + date
                    });
                }

                for (var j in lottery['second-prize']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['second-prize'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลที่ 2 งวดวันที่ : ' + date
                    });
                }


                for (var j in lottery['third-prize']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['third-prize'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลที่ 3 งวดวันที่ : ' + date
                    });
                }

                for (var j in lottery['four-prize']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['four-prize'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลที่ 4 งวดวันที่ : ' + date
                    });
                }

                for (var j in lottery['five-prize']) {
                    let ref = firebase.app().database().ref('/history/' + lottery['five-prize'][j]);
                    ref.update({
                        [year + '-' + month + '-' + day]: 'รางวัลที่ 5 งวดวันที่ : ' + date
                    });
                }

            }
        }
    });
}

function getData2(url, day, month, year, monthTH, date) { //แกะวิธีที่ 2 (กรณีวิธีแรกไม่ผ่าน จะวิ่งเข้าวิธีที่สอง)
    var lottery = {};
    lottery['date'] = {};
    lottery['first-prize'] = {};
    lottery['front3'] = {};
    lottery['back3'] = {};
    lottery['back2'] = {};
    lottery['nearby'] = {};
    lottery['second-prize'] = {};
    lottery['third-prize'] = {};
    lottery['four-prize'] = {};
    lottery['five-prize'] = {};
    request(url, function (error, response, html) {
        if (!error) {
            var $ = cheerio.load(html);
            try {
                //วันที่
                $('h5').filter(function () {
                    var data = $(this);
                    var a = data.children();
                    lottery['date'] = {
                        'date': a[0].children[0].data
                    }
                });

                //รางวัลที่ 1
                $('.first-prize').filter(function () {
                    var data2 = $(this);
                    var a1 = data2.children();
                    lottery['first-prize']['1'] = a1[1].children[0].data;
                });

                // เมื่อก่อนไม่มีรางวัลเลขหน้า 3 ตัว
                //รางวัลเลขหน้า 3 ตัว
                lottery['front3'] = "noData";

                //รางวัลเลขท้าย 3 ตัว
                $('.back3').filter(function () {
                    var data2 = $(this);
                    var a1 = data2.children();
                    var count = 1;
                    for (var i = 0; i < Object.keys(a1[1]).length; i++) {
                        if (typeof (a1[1].children[i]) !== 'undefined') {
                            if (Object.keys(a1[1].children[i]).length === 10)
                                lottery['back3'][count++] = a1[1].children[i].children[0].data;
                        }
                    }
                });

                //รางวัลเลขท้าย 2 ตัว
                $('.back2').filter(function () {
                    var data2 = $(this);
                    var a1 = data2.children();
                    lottery['back2']['1'] = a1[1].children[0].data;
                });

                //รางวัลใกล้เคียง => 2 รางวัล
                $('.nearby').filter(function () {
                    var data2 = $(this);
                    var b = data2.children();
                    lottery['nearby'] = {
                        '1': b[1].children[0].data,
                        '2': b[2].children[0].data
                    }
                });

                //รางวัลที่ 2  => 5 รางวัล
                $('.second-prize').filter(function () {
                    var data3 = $(this);
                    var c = data3.children();
                    for (var i = 1; i <= 5; i++) {
                        lottery['second-prize'][i] = c[i].children[0].data
                    }
                });


                //รางวัลที่ 3  => 10 รางวัล
                $('.third-prize').filter(function () {
                    var data4 = $(this);
                    var d = data4.children();
                    for (var i = 1; i <= 10; i++) {
                        lottery['third-prize'][i] = d[i].children[0].data
                    }
                });

                //รางวัลที่ 4  => 50 รางวัล
                $('.four-prize').filter(function () {
                    var data5 = $(this);
                    var e = data5.children();
                    for (var i = 1; i <= 50; i++) {
                        lottery['four-prize'][i] = e[i].children[0].data
                    }
                });

                //รางวัลที่ 5  => 100 รางวัล
                $('.five-prize').filter(function () {
                    var data6 = $(this);
                    var f = data6.children();
                    for (var i = 1; i <= 100; i++) {
                        lottery['five-prize'][i] = f[i].children[0].data
                    }
                });
            } catch (e) {
                console.log(url);
                lottery['date'] = "noData";
                lottery['first-prize'] = "noData";
                lottery['front3'] = "noData";
                lottery['back3'] = "noData";
                lottery['back2'] = "noData";
                lottery['nearby'] = {
                    '1': "noData",
                    '2': "noData"
                }
                for (var i = 1; i <= 5; i++) {
                    lottery['second-prize'][i] = "noData";
                }
                for (var i = 1; i <= 10; i++) {
                    lottery['third-prize'][i] = "noData";
                }
                for (var i = 1; i <= 50; i++) {
                    lottery['four-prize'][i] = "noData";
                }
                for (var i = 1; i <= 100; i++) {
                    lottery['five-prize'][i] = "noData";
                }
            }



            let ref = firebase.app().database().ref('/' + year + '/' + month + '/' + day);
            ref.set({
                'numericDate': year + '/' + month + '/' + day,
                'fullDate': date,
                'shortYear': year,
                'month': month,
                'date': day,
                'monthTH': monthTH,
                'url': url,
                'firstPrize': lottery['first-prize'],
                'front3': lottery['front3'],
                'back3': lottery['back3'],
                'back2': lottery['back2'],
                'nearby': lottery['nearby'],
                'second-prize': lottery['second-prize'],
                'third-prize': lottery['third-prize'],
                'four-prize': lottery['four-prize'],
                'five-prize': lottery['five-prize']
            });

            for (var j in lottery['first-prize']) {
                let ref = firebase.app().database().ref('/history/' + lottery['first-prize'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลที่ 1 งวดวันที่ : ' + date
                });
            }

            for (var j in lottery['front3']) {
                let ref = firebase.app().database().ref('/history/' + lottery['front3'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลเลขหน้า 3 ตัว งวดวันที่ : ' + date
                });
            }

            for (var j in lottery['back3']) {
                let ref = firebase.app().database().ref('/history/' + lottery['back3'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลเลขท้าย 3 ตัว งวดวันที่ : ' + date
                });
            }

            for (var j in lottery['back2']) {
                let ref = firebase.app().database().ref('/history/' + lottery['back2'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลเลขท้าย 2 ตัว งวดวันที่ : ' + date
                });
            }

            for (var j in lottery['nearby']) {
                let ref = firebase.app().database().ref('/history/' + lottery['nearby'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลรางวัลข้างเคียงรางวัลที่ 1 งวดวันที่ : ' + date
                });
            }

            for (var j in lottery['second-prize']) {
                let ref = firebase.app().database().ref('/history/' + lottery['second-prize'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลที่ 2 งวดวันที่ : ' + date
                });
            }


            for (var j in lottery['third-prize']) {
                let ref = firebase.app().database().ref('/history/' + lottery['third-prize'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลที่ 3 งวดวันที่ : ' + date
                });
            }

            for (var j in lottery['four-prize']) {
                let ref = firebase.app().database().ref('/history/' + lottery['four-prize'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลที่ 4 งวดวันที่ : ' + date
                });
            }

            for (var j in lottery['five-prize']) {
                let ref = firebase.app().database().ref('/history/' + lottery['five-prize'][j]);
                ref.update({
                    [year + '-' + month + '-' + day]: 'รางวัลที่ 5 งวดวันที่ : ' + date
                });
            }
        }
    });
}