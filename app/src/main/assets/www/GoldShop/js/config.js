var projectName = "Express";
var userBaseMsg = _COMMON.jsonP(localStorage.getItem("userBaseMsg"));
var userAddMsg = _COMMON.jsonP(localStorage.getItem("userAddMsg"));
var userWhiteMsg = _COMMON.jsonP(localStorage.getItem("userWhiteMsg"));
var repayAutoBankCard = _COMMON.jsonP(localStorage.getItem("repayAutoBankCard"));
$(function () {
	setTimeout(function(){
		$(".bar-subheader,.card-list,.detail-list,.card-list,.record-list").css("visibility","visible")
	},300);
	var trackConfig = {
		/*tnh_0101: {
			netTrack: true,
			event: "替你还",
			desc: "进入替你还"
		}*/
	};
	//函数的arguments转数组
	function arg2Arr(args) {
		return args.length === 1 ? [args[0]] : Array.apply(null, args);
	}
	$.fn.extend({
		//将$(obj).on('tap', fn)包装成$(obj).tap(fn)
		tap: function (target, func) {
			if(target) {
				return this.each(function() {
					$(this).on("tap", target, func);
				});
			}else {
				return this.each(function() {
					$(this).on("tap", func);
				});
			}
		}
	});
	$.extend({
		getParams: function() {
			var o = {};
			window.location.search.substring(1).split("&").forEach(function(v) {
				var t = v.split("=");
				o[t[0]] = t[1];
			});
			return o;
		},
		//$.doPlugin包装成一个函数形式
		createPlugin: function(config) {
			return function (next) {
				$.doPlugin(config, next);
			}
		},
		//插件方法抽取，默认是网络请求插件
		doPlugin: function(config, next) {
			var cfg = {};
			config = $.isArray(config) ? config[0] : config;
			for(var key in config) {
				if(config.hasOwnProperty(key) && "plugin|succ|fail|err|json".indexOf(key) == -1) {
					cfg[key] = config[key];
				}
			}
			var plugin = config.plugin || "doNetworkActionWithoutLoading";
			var isJsonReq = config.json == undefined || config.json;
			if(plugin == "doNetworkActionWithoutLoading" && !cfg.dataRequestType && isJsonReq) {
				cfg["dataRequestType"] = "JSON";
			}
			var succ = config.succ ? function(data) {
				if(config.plugin == "doNetworkActionWithoutLoading" || !config.plugin) {
					var rs = data.indexOf("\<\?xml") > -1 && $.xml2json ? $.xml2json(data) : JSON.parse(data);
					if(rs.respCode == "0000") {
						if(config.succ) {
							if(rs.data != undefined) {
								config.succ(rs.data, rs, next);
							}else {
								config.succ(rs, next);
							}
						}
					} else {
						config.fail && config.fail(rs, next);
					}
				} else {
					config.succ && config.succ(data, next);
				}
			} : null;
			var err = config.err ? function(errMsg) {
				config.err(errMsg, next);
			} : null;
			var fn = navigator.myPlugin[plugin + "Func"];
			if(fn) {
				fn([cfg], succ, err);
			}else {
				exec(succ, err, "MyPlugin", plugin, [cfg]);
			}
		},
		/**
		 * 链式调用一些函数
		 * compose(f, g, k) => f(g(k())) if k is function
		 * or compose(f, g, k) => f(g(k)) if k is not function
		 */
		compose: function() {
			var args = arg2Arr(arguments);
			if(args.length == 1) {
				if($.isFunction(args[0])) {
					return args[0]();
				}else {
					console.log('error arguments');
					return args[0];
				}
			}
			var rs = args[args.length - 1];
			console.log(rs);
			console.log(args.slice(0, args.length - 1).reverse());
			$.each(args.slice(0, args.length - 1).reverse(), function(_, fn) {
				rs = fn($.isFunction(rs) ? rs() : rs);
				console.log(rs)
			});
			return rs;
		},
		/**
		 * 串行执行函数
		 */
		serializeFn:function(){
			var args = arg2Arr(arguments);
			for(var i = 0, len = args.length; i < len;i++) {
				if(args[i+1]) {
					args[i].bind(this, args[i+1]);
				}
			}
			args[0]();
		},
		//链式的调用一个对象的函数其中可能全局的函数
		//todo: 可删除此函数
		fnsChain: function(obj, fns, val) {
			var rs = val;
			var g = window || global;
			var arr = null;
			var fn = null;
			$.each(fns.reverse(), function(_, v) {
				arr = v.split(" ");
				fn = arr[0];
				if(rs != undefined) {
					arr[0] = rs;
				}else {
					arr = arr.slice(1);
				}
				rs = (obj[fn] || g[fn]).apply(obj, arr);
			});
			return rs;
		},
		underTrack: function() {
			var args = arg2Arr(arguments);
			var eid = args[0];
			var curTrackCfg = trackConfig[eid];
			var succFn = args[1] || null;
			var failFn = args[2] || null;
			var errFn = args[3] || null;
			var fns = {};
			if(succFn) {
				fns.succ = succFn;
			}
			if(failFn) {
				fns.fail = failFn;
			}else if(succFn) {
				fns.fail = succFn;
			}
			if(errFn) {
				fns.err = errFn;
			}else if(failFn) {
				fns.err = failFn;
			} else if(succFn) {
				fns.err = succFn;
			}
			$.doPlugin({
				"plugin": "eventTrack",
				"eventId": eid
			});
			if(curTrackCfg && curTrackCfg.netTrack) {
				$.doPlugin(
						$.extend({
							"application":"CreditCardApplyFor.Req",
							"cumtomerId": userBaseMsg.cumtomerId,
							"bankName": curTrackCfg.desc,
							"clickStatus": eid
						}, fns)
				)
			}else {
				if(fns.succ) {
					fns.succ();
				}else if(fns.fail) {
					fns.fail();
				}else if(fns.err) {
					fns.err();
				}
			}
		}
	})
});