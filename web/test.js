(function f() {
    'use strict';
    var temp = 1;
    eval('temp = 123');
    console.log(temp);  // ReferenceError: foo is not defined
})();


(function f() {
    'use strict';
    var foo = 1;
    eval('foo = 2');
    console.log(foo);  // 2
})();