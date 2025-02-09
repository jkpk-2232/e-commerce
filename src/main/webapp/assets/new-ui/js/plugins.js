/*
 *  Document   : plugins.js
 *  Author     : Various
 *  Description: Plugins
 */
/* Avoid `console` errors in browsers that lack a console */
(function() {
    var e;
    var t = function() {};
    var n = ["assert", "clear", "count", "debug", "dir", "dirxml", "error", "exception", "group", "groupCollapsed", "groupEnd", "info", "log", "markTimeline", "profile", "profileEnd", "table", "time", "timeEnd", "timeStamp", "trace", "warn"];
    var r = n.length;
    var i = window.console = window.console || {};
    while (r--) {
        e = n[r];
        if (!i[e]) {
            i[e] = t
        }
    }
})();

/*!
 * jQuery UI - v1.10.3 - 2013-09-10
 * http://jqueryui.com
 * Includes: jquery.ui.core.js, jquery.ui.widget.js, jquery.ui.mouse.js, jquery.ui.draggable.js, jquery.ui.resizable.js
 * Copyright 2013 jQuery Foundation and other contributors; Licensed MIT
 */
(function(e, t) {
    function i(t, i) {
        var s, a, o, r = t.nodeName.toLowerCase();
        return "area" === r ? (s = t.parentNode, a = s.name, t.href && a && "map" === s.nodeName.toLowerCase() ? (o = e("img[usemap=#" + a + "]")[0], !!o && n(o)) : !1) : (/input|select|textarea|button|object/.test(r) ? !t.disabled : "a" === r ? t.href || i : i) && n(t)
    }

    function n(t) {
        return e.expr.filters.visible(t) && !e(t).parents().addBack().filter(function() {
            return "hidden" === e.css(this, "visibility")
        }).length
    }
    var s = 0,
        a = /^ui-id-\d+$/;
    e.ui = e.ui || {}, e.extend(e.ui, {
        version: "1.10.3",
        keyCode: {
            BACKSPACE: 8,
            COMMA: 188,
            DELETE: 46,
            DOWN: 40,
            END: 35,
            ENTER: 13,
            ESCAPE: 27,
            HOME: 36,
            LEFT: 37,
            NUMPAD_ADD: 107,
            NUMPAD_DECIMAL: 110,
            NUMPAD_DIVIDE: 111,
            NUMPAD_ENTER: 108,
            NUMPAD_MULTIPLY: 106,
            NUMPAD_SUBTRACT: 109,
            PAGE_DOWN: 34,
            PAGE_UP: 33,
            PERIOD: 190,
            RIGHT: 39,
            SPACE: 32,
            TAB: 9,
            UP: 38
        }
    }), e.fn.extend({
        focus: function(t) {
            return function(i, n) {
                return "number" == typeof i ? this.each(function() {
                    var t = this;
                    setTimeout(function() {
                        e(t).focus(), n && n.call(t)
                    }, i)
                }) : t.apply(this, arguments)
            }
        }(e.fn.focus),
        scrollParent: function() {
            var t;
            return t = e.ui.ie && /(static|relative)/.test(this.css("position")) || /absolute/.test(this.css("position")) ? this.parents().filter(function() {
                return /(relative|absolute|fixed)/.test(e.css(this, "position")) && /(auto|scroll)/.test(e.css(this, "overflow") + e.css(this, "overflow-y") + e.css(this, "overflow-x"))
            }).eq(0) : this.parents().filter(function() {
                return /(auto|scroll)/.test(e.css(this, "overflow") + e.css(this, "overflow-y") + e.css(this, "overflow-x"))
            }).eq(0), /fixed/.test(this.css("position")) || !t.length ? e(document) : t
        },
        zIndex: function(i) {
            if (i !== t) return this.css("zIndex", i);
            if (this.length)
                for (var n, s, a = e(this[0]); a.length && a[0] !== document;) {
                    if (n = a.css("position"), ("absolute" === n || "relative" === n || "fixed" === n) && (s = parseInt(a.css("zIndex"), 10), !isNaN(s) && 0 !== s)) return s;
                    a = a.parent()
                }
            return 0
        },
        uniqueId: function() {
            return this.each(function() {
                this.id || (this.id = "ui-id-" + ++s)
            })
        },
        removeUniqueId: function() {
            return this.each(function() {
                a.test(this.id) && e(this).removeAttr("id")
            })
        }
    }), e.extend(e.expr[":"], {
        data: e.expr.createPseudo ? e.expr.createPseudo(function(t) {
            return function(i) {
                return !!e.data(i, t)
            }
        }) : function(t, i, n) {
            return !!e.data(t, n[3])
        },
        focusable: function(t) {
            return i(t, !isNaN(e.attr(t, "tabindex")))
        },
        tabbable: function(t) {
            var n = e.attr(t, "tabindex"),
                s = isNaN(n);
            return (s || n >= 0) && i(t, !s)
        }
    }), e("<a>").outerWidth(1).jquery || e.each(["Width", "Height"], function(i, n) {
        function s(t, i, n, s) {
            return e.each(a, function() {
                i -= parseFloat(e.css(t, "padding" + this)) || 0, n && (i -= parseFloat(e.css(t, "border" + this + "Width")) || 0), s && (i -= parseFloat(e.css(t, "margin" + this)) || 0)
            }), i
        }
        var a = "Width" === n ? ["Left", "Right"] : ["Top", "Bottom"],
            o = n.toLowerCase(),
            r = {
                innerWidth: e.fn.innerWidth,
                innerHeight: e.fn.innerHeight,
                outerWidth: e.fn.outerWidth,
                outerHeight: e.fn.outerHeight
            };
        e.fn["inner" + n] = function(i) {
            return i === t ? r["inner" + n].call(this) : this.each(function() {
                e(this).css(o, s(this, i) + "px")
            })
        }, e.fn["outer" + n] = function(t, i) {
            return "number" != typeof t ? r["outer" + n].call(this, t) : this.each(function() {
                e(this).css(o, s(this, t, !0, i) + "px")
            })
        }
    }), e.fn.addBack || (e.fn.addBack = function(e) {
        return this.add(null == e ? this.prevObject : this.prevObject.filter(e))
    }), e("<a>").data("a-b", "a").removeData("a-b").data("a-b") && (e.fn.removeData = function(t) {
        return function(i) {
            return arguments.length ? t.call(this, e.camelCase(i)) : t.call(this)
        }
    }(e.fn.removeData)), e.ui.ie = !!/msie [\w.]+/.exec(navigator.userAgent.toLowerCase()), e.support.selectstart = "onselectstart" in document.createElement("div"), e.fn.extend({
        disableSelection: function() {
            return this.bind((e.support.selectstart ? "selectstart" : "mousedown") + ".ui-disableSelection", function(e) {
                e.preventDefault()
            })
        },
        enableSelection: function() {
            return this.unbind(".ui-disableSelection")
        }
    }), e.extend(e.ui, {
        plugin: {
            add: function(t, i, n) {
                var s, a = e.ui[t].prototype;
                for (s in n) a.plugins[s] = a.plugins[s] || [], a.plugins[s].push([i, n[s]])
            },
            call: function(e, t, i) {
                var n, s = e.plugins[t];
                if (s && e.element[0].parentNode && 11 !== e.element[0].parentNode.nodeType)
                    for (n = 0; s.length > n; n++) e.options[s[n][0]] && s[n][1].apply(e.element, i)
            }
        },
        hasScroll: function(t, i) {
            if ("hidden" === e(t).css("overflow")) return !1;
            var n = i && "left" === i ? "scrollLeft" : "scrollTop",
                s = !1;
            return t[n] > 0 ? !0 : (t[n] = 1, s = t[n] > 0, t[n] = 0, s)
        }
    })
})(jQuery);
(function(t, e) {
    var i = 0,
        s = Array.prototype.slice,
        n = t.cleanData;
    t.cleanData = function(e) {
        for (var i, s = 0; null != (i = e[s]); s++) try {
            t(i).triggerHandler("remove")
        } catch (o) {}
        n(e)
    }, t.widget = function(i, s, n) {
        var o, a, r, h, l = {},
            c = i.split(".")[0];
        i = i.split(".")[1], o = c + "-" + i, n || (n = s, s = t.Widget), t.expr[":"][o.toLowerCase()] = function(e) {
            return !!t.data(e, o)
        }, t[c] = t[c] || {}, a = t[c][i], r = t[c][i] = function(t, i) {
            return this._createWidget ? (arguments.length && this._createWidget(t, i), e) : new r(t, i)
        }, t.extend(r, a, {
            version: n.version,
            _proto: t.extend({}, n),
            _childConstructors: []
        }), h = new s, h.options = t.widget.extend({}, h.options), t.each(n, function(i, n) {
            return t.isFunction(n) ? (l[i] = function() {
                var t = function() {
                        return s.prototype[i].apply(this, arguments)
                    },
                    e = function(t) {
                        return s.prototype[i].apply(this, t)
                    };
                return function() {
                    var i, s = this._super,
                        o = this._superApply;
                    return this._super = t, this._superApply = e, i = n.apply(this, arguments), this._super = s, this._superApply = o, i
                }
            }(), e) : (l[i] = n, e)
        }), r.prototype = t.widget.extend(h, {
            widgetEventPrefix: a ? h.widgetEventPrefix : i
        }, l, {
            constructor: r,
            namespace: c,
            widgetName: i,
            widgetFullName: o
        }), a ? (t.each(a._childConstructors, function(e, i) {
            var s = i.prototype;
            t.widget(s.namespace + "." + s.widgetName, r, i._proto)
        }), delete a._childConstructors) : s._childConstructors.push(r), t.widget.bridge(i, r)
    }, t.widget.extend = function(i) {
        for (var n, o, a = s.call(arguments, 1), r = 0, h = a.length; h > r; r++)
            for (n in a[r]) o = a[r][n], a[r].hasOwnProperty(n) && o !== e && (i[n] = t.isPlainObject(o) ? t.isPlainObject(i[n]) ? t.widget.extend({}, i[n], o) : t.widget.extend({}, o) : o);
        return i
    }, t.widget.bridge = function(i, n) {
        var o = n.prototype.widgetFullName || i;
        t.fn[i] = function(a) {
            var r = "string" == typeof a,
                h = s.call(arguments, 1),
                l = this;
            return a = !r && h.length ? t.widget.extend.apply(null, [a].concat(h)) : a, r ? this.each(function() {
                var s, n = t.data(this, o);
                return n ? t.isFunction(n[a]) && "_" !== a.charAt(0) ? (s = n[a].apply(n, h), s !== n && s !== e ? (l = s && s.jquery ? l.pushStack(s.get()) : s, !1) : e) : t.error("no such method '" + a + "' for " + i + " widget instance") : t.error("cannot call methods on " + i + " prior to initialization; " + "attempted to call method '" + a + "'")
            }) : this.each(function() {
                var e = t.data(this, o);
                e ? e.option(a || {})._init() : t.data(this, o, new n(a, this))
            }), l
        }
    }, t.Widget = function() {}, t.Widget._childConstructors = [], t.Widget.prototype = {
        widgetName: "widget",
        widgetEventPrefix: "",
        defaultElement: "<div>",
        options: {
            disabled: !1,
            create: null
        },
        _createWidget: function(e, s) {
            s = t(s || this.defaultElement || this)[0], this.element = t(s), this.uuid = i++, this.eventNamespace = "." + this.widgetName + this.uuid, this.options = t.widget.extend({}, this.options, this._getCreateOptions(), e), this.bindings = t(), this.hoverable = t(), this.focusable = t(), s !== this && (t.data(s, this.widgetFullName, this), this._on(!0, this.element, {
                remove: function(t) {
                    t.target === s && this.destroy()
                }
            }), this.document = t(s.style ? s.ownerDocument : s.document || s), this.window = t(this.document[0].defaultView || this.document[0].parentWindow)), this._create(), this._trigger("create", null, this._getCreateEventData()), this._init()
        },
        _getCreateOptions: t.noop,
        _getCreateEventData: t.noop,
        _create: t.noop,
        _init: t.noop,
        destroy: function() {
            this._destroy(), this.element.unbind(this.eventNamespace).removeData(this.widgetName).removeData(this.widgetFullName).removeData(t.camelCase(this.widgetFullName)), this.widget().unbind(this.eventNamespace).removeAttr("aria-disabled").removeClass(this.widgetFullName + "-disabled " + "ui-state-disabled"), this.bindings.unbind(this.eventNamespace), this.hoverable.removeClass("ui-state-hover"), this.focusable.removeClass("ui-state-focus")
        },
        _destroy: t.noop,
        widget: function() {
            return this.element
        },
        option: function(i, s) {
            var n, o, a, r = i;
            if (0 === arguments.length) return t.widget.extend({}, this.options);
            if ("string" == typeof i)
                if (r = {}, n = i.split("."), i = n.shift(), n.length) {
                    for (o = r[i] = t.widget.extend({}, this.options[i]), a = 0; n.length - 1 > a; a++) o[n[a]] = o[n[a]] || {}, o = o[n[a]];
                    if (i = n.pop(), s === e) return o[i] === e ? null : o[i];
                    o[i] = s
                } else {
                    if (s === e) return this.options[i] === e ? null : this.options[i];
                    r[i] = s
                }
            return this._setOptions(r), this
        },
        _setOptions: function(t) {
            var e;
            for (e in t) this._setOption(e, t[e]);
            return this
        },
        _setOption: function(t, e) {
            return this.options[t] = e, "disabled" === t && (this.widget().toggleClass(this.widgetFullName + "-disabled ui-state-disabled", !!e).attr("aria-disabled", e), this.hoverable.removeClass("ui-state-hover"), this.focusable.removeClass("ui-state-focus")), this
        },
        enable: function() {
            return this._setOption("disabled", !1)
        },
        disable: function() {
            return this._setOption("disabled", !0)
        },
        _on: function(i, s, n) {
            var o, a = this;
            "boolean" != typeof i && (n = s, s = i, i = !1), n ? (s = o = t(s), this.bindings = this.bindings.add(s)) : (n = s, s = this.element, o = this.widget()), t.each(n, function(n, r) {
                function h() {
                    return i || a.options.disabled !== !0 && !t(this).hasClass("ui-state-disabled") ? ("string" == typeof r ? a[r] : r).apply(a, arguments) : e
                }
                "string" != typeof r && (h.guid = r.guid = r.guid || h.guid || t.guid++);
                var l = n.match(/^(\w+)\s*(.*)$/),
                    c = l[1] + a.eventNamespace,
                    u = l[2];
                u ? o.delegate(u, c, h) : s.bind(c, h)
            })
        },
        _off: function(t, e) {
            e = (e || "").split(" ").join(this.eventNamespace + " ") + this.eventNamespace, t.unbind(e).undelegate(e)
        },
        _delay: function(t, e) {
            function i() {
                return ("string" == typeof t ? s[t] : t).apply(s, arguments)
            }
            var s = this;
            return setTimeout(i, e || 0)
        },
        _hoverable: function(e) {
            this.hoverable = this.hoverable.add(e), this._on(e, {
                mouseenter: function(e) {
                    t(e.currentTarget).addClass("ui-state-hover")
                },
                mouseleave: function(e) {
                    t(e.currentTarget).removeClass("ui-state-hover")
                }
            })
        },
        _focusable: function(e) {
            this.focusable = this.focusable.add(e), this._on(e, {
                focusin: function(e) {
                    t(e.currentTarget).addClass("ui-state-focus")
                },
                focusout: function(e) {
                    t(e.currentTarget).removeClass("ui-state-focus")
                }
            })
        },
        _trigger: function(e, i, s) {
            var n, o, a = this.options[e];
            if (s = s || {}, i = t.Event(i), i.type = (e === this.widgetEventPrefix ? e : this.widgetEventPrefix + e).toLowerCase(), i.target = this.element[0], o = i.originalEvent)
                for (n in o) n in i || (i[n] = o[n]);
            return this.element.trigger(i, s), !(t.isFunction(a) && a.apply(this.element[0], [i].concat(s)) === !1 || i.isDefaultPrevented())
        }
    }, t.each({
        show: "fadeIn",
        hide: "fadeOut"
    }, function(e, i) {
        t.Widget.prototype["_" + e] = function(s, n, o) {
            "string" == typeof n && (n = {
                effect: n
            });
            var a, r = n ? n === !0 || "number" == typeof n ? i : n.effect || i : e;
            n = n || {}, "number" == typeof n && (n = {
                duration: n
            }), a = !t.isEmptyObject(n), n.complete = o, n.delay && s.delay(n.delay), a && t.effects && t.effects.effect[r] ? s[e](n) : r !== e && s[r] ? s[r](n.duration, n.easing, o) : s.queue(function(i) {
                t(this)[e](), o && o.call(s[0]), i()
            })
        }
    })
})(jQuery);
(function(t) {
    var e = !1;
    t(document).mouseup(function() {
        e = !1
    }), t.widget("ui.mouse", {
        version: "1.10.3",
        options: {
            cancel: "input,textarea,button,select,option",
            distance: 1,
            delay: 0
        },
        _mouseInit: function() {
            var e = this;
            this.element.bind("mousedown." + this.widgetName, function(t) {
                return e._mouseDown(t)
            }).bind("click." + this.widgetName, function(i) {
                return !0 === t.data(i.target, e.widgetName + ".preventClickEvent") ? (t.removeData(i.target, e.widgetName + ".preventClickEvent"), i.stopImmediatePropagation(), !1) : undefined
            }), this.started = !1
        },
        _mouseDestroy: function() {
            this.element.unbind("." + this.widgetName), this._mouseMoveDelegate && t(document).unbind("mousemove." + this.widgetName, this._mouseMoveDelegate).unbind("mouseup." + this.widgetName, this._mouseUpDelegate)
        },
        _mouseDown: function(i) {
            if (!e) {
                this._mouseStarted && this._mouseUp(i), this._mouseDownEvent = i;
                var s = this,
                    n = 1 === i.which,
                    a = "string" == typeof this.options.cancel && i.target.nodeName ? t(i.target).closest(this.options.cancel).length : !1;
                return n && !a && this._mouseCapture(i) ? (this.mouseDelayMet = !this.options.delay, this.mouseDelayMet || (this._mouseDelayTimer = setTimeout(function() {
                    s.mouseDelayMet = !0
                }, this.options.delay)), this._mouseDistanceMet(i) && this._mouseDelayMet(i) && (this._mouseStarted = this._mouseStart(i) !== !1, !this._mouseStarted) ? (i.preventDefault(), !0) : (!0 === t.data(i.target, this.widgetName + ".preventClickEvent") && t.removeData(i.target, this.widgetName + ".preventClickEvent"), this._mouseMoveDelegate = function(t) {
                    return s._mouseMove(t)
                }, this._mouseUpDelegate = function(t) {
                    return s._mouseUp(t)
                }, t(document).bind("mousemove." + this.widgetName, this._mouseMoveDelegate).bind("mouseup." + this.widgetName, this._mouseUpDelegate), i.preventDefault(), e = !0, !0)) : !0
            }
        },
        _mouseMove: function(e) {
            return t.ui.ie && (!document.documentMode || 9 > document.documentMode) && !e.button ? this._mouseUp(e) : this._mouseStarted ? (this._mouseDrag(e), e.preventDefault()) : (this._mouseDistanceMet(e) && this._mouseDelayMet(e) && (this._mouseStarted = this._mouseStart(this._mouseDownEvent, e) !== !1, this._mouseStarted ? this._mouseDrag(e) : this._mouseUp(e)), !this._mouseStarted)
        },
        _mouseUp: function(e) {
            return t(document).unbind("mousemove." + this.widgetName, this._mouseMoveDelegate).unbind("mouseup." + this.widgetName, this._mouseUpDelegate), this._mouseStarted && (this._mouseStarted = !1, e.target === this._mouseDownEvent.target && t.data(e.target, this.widgetName + ".preventClickEvent", !0), this._mouseStop(e)), !1
        },
        _mouseDistanceMet: function(t) {
            return Math.max(Math.abs(this._mouseDownEvent.pageX - t.pageX), Math.abs(this._mouseDownEvent.pageY - t.pageY)) >= this.options.distance
        },
        _mouseDelayMet: function() {
            return this.mouseDelayMet
        },
        _mouseStart: function() {},
        _mouseDrag: function() {},
        _mouseStop: function() {},
        _mouseCapture: function() {
            return !0
        }
    })
})(jQuery);
(function(t) {
    t.widget("ui.draggable", t.ui.mouse, {
        version: "1.10.3",
        widgetEventPrefix: "drag",
        options: {
            addClasses: !0,
            appendTo: "parent",
            axis: !1,
            connectToSortable: !1,
            containment: !1,
            cursor: "auto",
            cursorAt: !1,
            grid: !1,
            handle: !1,
            helper: "original",
            iframeFix: !1,
            opacity: !1,
            refreshPositions: !1,
            revert: !1,
            revertDuration: 500,
            scope: "default",
            scroll: !0,
            scrollSensitivity: 20,
            scrollSpeed: 20,
            snap: !1,
            snapMode: "both",
            snapTolerance: 20,
            stack: !1,
            zIndex: !1,
            drag: null,
            start: null,
            stop: null
        },
        _create: function() {
            "original" !== this.options.helper || /^(?:r|a|f)/.test(this.element.css("position")) || (this.element[0].style.position = "relative"), this.options.addClasses && this.element.addClass("ui-draggable"), this.options.disabled && this.element.addClass("ui-draggable-disabled"), this._mouseInit()
        },
        _destroy: function() {
            this.element.removeClass("ui-draggable ui-draggable-dragging ui-draggable-disabled"), this._mouseDestroy()
        },
        _mouseCapture: function(e) {
            var i = this.options;
            return this.helper || i.disabled || t(e.target).closest(".ui-resizable-handle").length > 0 ? !1 : (this.handle = this._getHandle(e), this.handle ? (t(i.iframeFix === !0 ? "iframe" : i.iframeFix).each(function() {
                t("<div class='ui-draggable-iframeFix' style='background: #fff;'></div>").css({
                    width: this.offsetWidth + "px",
                    height: this.offsetHeight + "px",
                    position: "absolute",
                    opacity: "0.001",
                    zIndex: 1e3
                }).css(t(this).offset()).appendTo("body")
            }), !0) : !1)
        },
        _mouseStart: function(e) {
            var i = this.options;
            return this.helper = this._createHelper(e), this.helper.addClass("ui-draggable-dragging"), this._cacheHelperProportions(), t.ui.ddmanager && (t.ui.ddmanager.current = this), this._cacheMargins(), this.cssPosition = this.helper.css("position"), this.scrollParent = this.helper.scrollParent(), this.offsetParent = this.helper.offsetParent(), this.offsetParentCssPosition = this.offsetParent.css("position"), this.offset = this.positionAbs = this.element.offset(), this.offset = {
                top: this.offset.top - this.margins.top,
                left: this.offset.left - this.margins.left
            }, this.offset.scroll = !1, t.extend(this.offset, {
                click: {
                    left: e.pageX - this.offset.left,
                    top: e.pageY - this.offset.top
                },
                parent: this._getParentOffset(),
                relative: this._getRelativeOffset()
            }), this.originalPosition = this.position = this._generatePosition(e), this.originalPageX = e.pageX, this.originalPageY = e.pageY, i.cursorAt && this._adjustOffsetFromHelper(i.cursorAt), this._setContainment(), this._trigger("start", e) === !1 ? (this._clear(), !1) : (this._cacheHelperProportions(), t.ui.ddmanager && !i.dropBehaviour && t.ui.ddmanager.prepareOffsets(this, e), this._mouseDrag(e, !0), t.ui.ddmanager && t.ui.ddmanager.dragStart(this, e), !0)
        },
        _mouseDrag: function(e, i) {
            if ("fixed" === this.offsetParentCssPosition && (this.offset.parent = this._getParentOffset()), this.position = this._generatePosition(e), this.positionAbs = this._convertPositionTo("absolute"), !i) {
                var s = this._uiHash();
                if (this._trigger("drag", e, s) === !1) return this._mouseUp({}), !1;
                this.position = s.position
            }
            return this.options.axis && "y" === this.options.axis || (this.helper[0].style.left = this.position.left + "px"), this.options.axis && "x" === this.options.axis || (this.helper[0].style.top = this.position.top + "px"), t.ui.ddmanager && t.ui.ddmanager.drag(this, e), !1
        },
        _mouseStop: function(e) {
            var i = this,
                s = !1;
            return t.ui.ddmanager && !this.options.dropBehaviour && (s = t.ui.ddmanager.drop(this, e)), this.dropped && (s = this.dropped, this.dropped = !1), "original" !== this.options.helper || t.contains(this.element[0].ownerDocument, this.element[0]) ? ("invalid" === this.options.revert && !s || "valid" === this.options.revert && s || this.options.revert === !0 || t.isFunction(this.options.revert) && this.options.revert.call(this.element, s) ? t(this.helper).animate(this.originalPosition, parseInt(this.options.revertDuration, 10), function() {
                i._trigger("stop", e) !== !1 && i._clear()
            }) : this._trigger("stop", e) !== !1 && this._clear(), !1) : !1
        },
        _mouseUp: function(e) {
            return t("div.ui-draggable-iframeFix").each(function() {
                this.parentNode.removeChild(this)
            }), t.ui.ddmanager && t.ui.ddmanager.dragStop(this, e), t.ui.mouse.prototype._mouseUp.call(this, e)
        },
        cancel: function() {
            return this.helper.is(".ui-draggable-dragging") ? this._mouseUp({}) : this._clear(), this
        },
        _getHandle: function(e) {
            return this.options.handle ? !!t(e.target).closest(this.element.find(this.options.handle)).length : !0
        },
        _createHelper: function(e) {
            var i = this.options,
                s = t.isFunction(i.helper) ? t(i.helper.apply(this.element[0], [e])) : "clone" === i.helper ? this.element.clone().removeAttr("id") : this.element;
            return s.parents("body").length || s.appendTo("parent" === i.appendTo ? this.element[0].parentNode : i.appendTo), s[0] === this.element[0] || /(fixed|absolute)/.test(s.css("position")) || s.css("position", "absolute"), s
        },
        _adjustOffsetFromHelper: function(e) {
            "string" == typeof e && (e = e.split(" ")), t.isArray(e) && (e = {
                left: +e[0],
                top: +e[1] || 0
            }), "left" in e && (this.offset.click.left = e.left + this.margins.left), "right" in e && (this.offset.click.left = this.helperProportions.width - e.right + this.margins.left), "top" in e && (this.offset.click.top = e.top + this.margins.top), "bottom" in e && (this.offset.click.top = this.helperProportions.height - e.bottom + this.margins.top)
        },
        _getParentOffset: function() {
            var e = this.offsetParent.offset();
            return "absolute" === this.cssPosition && this.scrollParent[0] !== document && t.contains(this.scrollParent[0], this.offsetParent[0]) && (e.left += this.scrollParent.scrollLeft(), e.top += this.scrollParent.scrollTop()), (this.offsetParent[0] === document.body || this.offsetParent[0].tagName && "html" === this.offsetParent[0].tagName.toLowerCase() && t.ui.ie) && (e = {
                top: 0,
                left: 0
            }), {
                top: e.top + (parseInt(this.offsetParent.css("borderTopWidth"), 10) || 0),
                left: e.left + (parseInt(this.offsetParent.css("borderLeftWidth"), 10) || 0)
            }
        },
        _getRelativeOffset: function() {
            if ("relative" === this.cssPosition) {
                var t = this.element.position();
                return {
                    top: t.top - (parseInt(this.helper.css("top"), 10) || 0) + this.scrollParent.scrollTop(),
                    left: t.left - (parseInt(this.helper.css("left"), 10) || 0) + this.scrollParent.scrollLeft()
                }
            }
            return {
                top: 0,
                left: 0
            }
        },
        _cacheMargins: function() {
            this.margins = {
                left: parseInt(this.element.css("marginLeft"), 10) || 0,
                top: parseInt(this.element.css("marginTop"), 10) || 0,
                right: parseInt(this.element.css("marginRight"), 10) || 0,
                bottom: parseInt(this.element.css("marginBottom"), 10) || 0
            }
        },
        _cacheHelperProportions: function() {
            this.helperProportions = {
                width: this.helper.outerWidth(),
                height: this.helper.outerHeight()
            }
        },
        _setContainment: function() {
            var e, i, s, n = this.options;
            return n.containment ? "window" === n.containment ? (this.containment = [t(window).scrollLeft() - this.offset.relative.left - this.offset.parent.left, t(window).scrollTop() - this.offset.relative.top - this.offset.parent.top, t(window).scrollLeft() + t(window).width() - this.helperProportions.width - this.margins.left, t(window).scrollTop() + (t(window).height() || document.body.parentNode.scrollHeight) - this.helperProportions.height - this.margins.top], undefined) : "document" === n.containment ? (this.containment = [0, 0, t(document).width() - this.helperProportions.width - this.margins.left, (t(document).height() || document.body.parentNode.scrollHeight) - this.helperProportions.height - this.margins.top], undefined) : n.containment.constructor === Array ? (this.containment = n.containment, undefined) : ("parent" === n.containment && (n.containment = this.helper[0].parentNode), i = t(n.containment), s = i[0], s && (e = "hidden" !== i.css("overflow"), this.containment = [(parseInt(i.css("borderLeftWidth"), 10) || 0) + (parseInt(i.css("paddingLeft"), 10) || 0), (parseInt(i.css("borderTopWidth"), 10) || 0) + (parseInt(i.css("paddingTop"), 10) || 0), (e ? Math.max(s.scrollWidth, s.offsetWidth) : s.offsetWidth) - (parseInt(i.css("borderRightWidth"), 10) || 0) - (parseInt(i.css("paddingRight"), 10) || 0) - this.helperProportions.width - this.margins.left - this.margins.right, (e ? Math.max(s.scrollHeight, s.offsetHeight) : s.offsetHeight) - (parseInt(i.css("borderBottomWidth"), 10) || 0) - (parseInt(i.css("paddingBottom"), 10) || 0) - this.helperProportions.height - this.margins.top - this.margins.bottom], this.relative_container = i), undefined) : (this.containment = null, undefined)
        },
        _convertPositionTo: function(e, i) {
            i || (i = this.position);
            var s = "absolute" === e ? 1 : -1,
                n = "absolute" !== this.cssPosition || this.scrollParent[0] !== document && t.contains(this.scrollParent[0], this.offsetParent[0]) ? this.scrollParent : this.offsetParent;
            return this.offset.scroll || (this.offset.scroll = {
                top: n.scrollTop(),
                left: n.scrollLeft()
            }), {
                top: i.top + this.offset.relative.top * s + this.offset.parent.top * s - ("fixed" === this.cssPosition ? -this.scrollParent.scrollTop() : this.offset.scroll.top) * s,
                left: i.left + this.offset.relative.left * s + this.offset.parent.left * s - ("fixed" === this.cssPosition ? -this.scrollParent.scrollLeft() : this.offset.scroll.left) * s
            }
        },
        _generatePosition: function(e) {
            var i, s, n, a, o = this.options,
                r = "absolute" !== this.cssPosition || this.scrollParent[0] !== document && t.contains(this.scrollParent[0], this.offsetParent[0]) ? this.scrollParent : this.offsetParent,
                l = e.pageX,
                h = e.pageY;
            return this.offset.scroll || (this.offset.scroll = {
                top: r.scrollTop(),
                left: r.scrollLeft()
            }), this.originalPosition && (this.containment && (this.relative_container ? (s = this.relative_container.offset(), i = [this.containment[0] + s.left, this.containment[1] + s.top, this.containment[2] + s.left, this.containment[3] + s.top]) : i = this.containment, e.pageX - this.offset.click.left < i[0] && (l = i[0] + this.offset.click.left), e.pageY - this.offset.click.top < i[1] && (h = i[1] + this.offset.click.top), e.pageX - this.offset.click.left > i[2] && (l = i[2] + this.offset.click.left), e.pageY - this.offset.click.top > i[3] && (h = i[3] + this.offset.click.top)), o.grid && (n = o.grid[1] ? this.originalPageY + Math.round((h - this.originalPageY) / o.grid[1]) * o.grid[1] : this.originalPageY, h = i ? n - this.offset.click.top >= i[1] || n - this.offset.click.top > i[3] ? n : n - this.offset.click.top >= i[1] ? n - o.grid[1] : n + o.grid[1] : n, a = o.grid[0] ? this.originalPageX + Math.round((l - this.originalPageX) / o.grid[0]) * o.grid[0] : this.originalPageX, l = i ? a - this.offset.click.left >= i[0] || a - this.offset.click.left > i[2] ? a : a - this.offset.click.left >= i[0] ? a - o.grid[0] : a + o.grid[0] : a)), {
                top: h - this.offset.click.top - this.offset.relative.top - this.offset.parent.top + ("fixed" === this.cssPosition ? -this.scrollParent.scrollTop() : this.offset.scroll.top),
                left: l - this.offset.click.left - this.offset.relative.left - this.offset.parent.left + ("fixed" === this.cssPosition ? -this.scrollParent.scrollLeft() : this.offset.scroll.left)
            }
        },
        _clear: function() {
            this.helper.removeClass("ui-draggable-dragging"), this.helper[0] === this.element[0] || this.cancelHelperRemoval || this.helper.remove(), this.helper = null, this.cancelHelperRemoval = !1
        },
        _trigger: function(e, i, s) {
            return s = s || this._uiHash(), t.ui.plugin.call(this, e, [i, s]), "drag" === e && (this.positionAbs = this._convertPositionTo("absolute")), t.Widget.prototype._trigger.call(this, e, i, s)
        },
        plugins: {},
        _uiHash: function() {
            return {
                helper: this.helper,
                position: this.position,
                originalPosition: this.originalPosition,
                offset: this.positionAbs
            }
        }
    }), t.ui.plugin.add("draggable", "connectToSortable", {
        start: function(e, i) {
            var s = t(this).data("ui-draggable"),
                n = s.options,
                a = t.extend({}, i, {
                    item: s.element
                });
            s.sortables = [], t(n.connectToSortable).each(function() {
                var i = t.data(this, "ui-sortable");
                i && !i.options.disabled && (s.sortables.push({
                    instance: i,
                    shouldRevert: i.options.revert
                }), i.refreshPositions(), i._trigger("activate", e, a))
            })
        },
        stop: function(e, i) {
            var s = t(this).data("ui-draggable"),
                n = t.extend({}, i, {
                    item: s.element
                });
            t.each(s.sortables, function() {
                this.instance.isOver ? (this.instance.isOver = 0, s.cancelHelperRemoval = !0, this.instance.cancelHelperRemoval = !1, this.shouldRevert && (this.instance.options.revert = this.shouldRevert), this.instance._mouseStop(e), this.instance.options.helper = this.instance.options._helper, "original" === s.options.helper && this.instance.currentItem.css({
                    top: "auto",
                    left: "auto"
                })) : (this.instance.cancelHelperRemoval = !1, this.instance._trigger("deactivate", e, n))
            })
        },
        drag: function(e, i) {
            var s = t(this).data("ui-draggable"),
                n = this;
            t.each(s.sortables, function() {
                var a = !1,
                    o = this;
                this.instance.positionAbs = s.positionAbs, this.instance.helperProportions = s.helperProportions, this.instance.offset.click = s.offset.click, this.instance._intersectsWith(this.instance.containerCache) && (a = !0, t.each(s.sortables, function() {
                    return this.instance.positionAbs = s.positionAbs, this.instance.helperProportions = s.helperProportions, this.instance.offset.click = s.offset.click, this !== o && this.instance._intersectsWith(this.instance.containerCache) && t.contains(o.instance.element[0], this.instance.element[0]) && (a = !1), a
                })), a ? (this.instance.isOver || (this.instance.isOver = 1, this.instance.currentItem = t(n).clone().removeAttr("id").appendTo(this.instance.element).data("ui-sortable-item", !0), this.instance.options._helper = this.instance.options.helper, this.instance.options.helper = function() {
                    return i.helper[0]
                }, e.target = this.instance.currentItem[0], this.instance._mouseCapture(e, !0), this.instance._mouseStart(e, !0, !0), this.instance.offset.click.top = s.offset.click.top, this.instance.offset.click.left = s.offset.click.left, this.instance.offset.parent.left -= s.offset.parent.left - this.instance.offset.parent.left, this.instance.offset.parent.top -= s.offset.parent.top - this.instance.offset.parent.top, s._trigger("toSortable", e), s.dropped = this.instance.element, s.currentItem = s.element, this.instance.fromOutside = s), this.instance.currentItem && this.instance._mouseDrag(e)) : this.instance.isOver && (this.instance.isOver = 0, this.instance.cancelHelperRemoval = !0, this.instance.options.revert = !1, this.instance._trigger("out", e, this.instance._uiHash(this.instance)), this.instance._mouseStop(e, !0), this.instance.options.helper = this.instance.options._helper, this.instance.currentItem.remove(), this.instance.placeholder && this.instance.placeholder.remove(), s._trigger("fromSortable", e), s.dropped = !1)
            })
        }
    }), t.ui.plugin.add("draggable", "cursor", {
        start: function() {
            var e = t("body"),
                i = t(this).data("ui-draggable").options;
            e.css("cursor") && (i._cursor = e.css("cursor")), e.css("cursor", i.cursor)
        },
        stop: function() {
            var e = t(this).data("ui-draggable").options;
            e._cursor && t("body").css("cursor", e._cursor)
        }
    }), t.ui.plugin.add("draggable", "opacity", {
        start: function(e, i) {
            var s = t(i.helper),
                n = t(this).data("ui-draggable").options;
            s.css("opacity") && (n._opacity = s.css("opacity")), s.css("opacity", n.opacity)
        },
        stop: function(e, i) {
            var s = t(this).data("ui-draggable").options;
            s._opacity && t(i.helper).css("opacity", s._opacity)
        }
    }), t.ui.plugin.add("draggable", "scroll", {
        start: function() {
            var e = t(this).data("ui-draggable");
            e.scrollParent[0] !== document && "HTML" !== e.scrollParent[0].tagName && (e.overflowOffset = e.scrollParent.offset())
        },
        drag: function(e) {
            var i = t(this).data("ui-draggable"),
                s = i.options,
                n = !1;
            i.scrollParent[0] !== document && "HTML" !== i.scrollParent[0].tagName ? (s.axis && "x" === s.axis || (i.overflowOffset.top + i.scrollParent[0].offsetHeight - e.pageY < s.scrollSensitivity ? i.scrollParent[0].scrollTop = n = i.scrollParent[0].scrollTop + s.scrollSpeed : e.pageY - i.overflowOffset.top < s.scrollSensitivity && (i.scrollParent[0].scrollTop = n = i.scrollParent[0].scrollTop - s.scrollSpeed)), s.axis && "y" === s.axis || (i.overflowOffset.left + i.scrollParent[0].offsetWidth - e.pageX < s.scrollSensitivity ? i.scrollParent[0].scrollLeft = n = i.scrollParent[0].scrollLeft + s.scrollSpeed : e.pageX - i.overflowOffset.left < s.scrollSensitivity && (i.scrollParent[0].scrollLeft = n = i.scrollParent[0].scrollLeft - s.scrollSpeed))) : (s.axis && "x" === s.axis || (e.pageY - t(document).scrollTop() < s.scrollSensitivity ? n = t(document).scrollTop(t(document).scrollTop() - s.scrollSpeed) : t(window).height() - (e.pageY - t(document).scrollTop()) < s.scrollSensitivity && (n = t(document).scrollTop(t(document).scrollTop() + s.scrollSpeed))), s.axis && "y" === s.axis || (e.pageX - t(document).scrollLeft() < s.scrollSensitivity ? n = t(document).scrollLeft(t(document).scrollLeft() - s.scrollSpeed) : t(window).width() - (e.pageX - t(document).scrollLeft()) < s.scrollSensitivity && (n = t(document).scrollLeft(t(document).scrollLeft() + s.scrollSpeed)))), n !== !1 && t.ui.ddmanager && !s.dropBehaviour && t.ui.ddmanager.prepareOffsets(i, e)
        }
    }), t.ui.plugin.add("draggable", "snap", {
        start: function() {
            var e = t(this).data("ui-draggable"),
                i = e.options;
            e.snapElements = [], t(i.snap.constructor !== String ? i.snap.items || ":data(ui-draggable)" : i.snap).each(function() {
                var i = t(this),
                    s = i.offset();
                this !== e.element[0] && e.snapElements.push({
                    item: this,
                    width: i.outerWidth(),
                    height: i.outerHeight(),
                    top: s.top,
                    left: s.left
                })
            })
        },
        drag: function(e, i) {
            var s, n, a, o, r, l, h, c, u, d, p = t(this).data("ui-draggable"),
                g = p.options,
                f = g.snapTolerance,
                m = i.offset.left,
                _ = m + p.helperProportions.width,
                v = i.offset.top,
                b = v + p.helperProportions.height;
            for (u = p.snapElements.length - 1; u >= 0; u--) r = p.snapElements[u].left, l = r + p.snapElements[u].width, h = p.snapElements[u].top, c = h + p.snapElements[u].height, r - f > _ || m > l + f || h - f > b || v > c + f || !t.contains(p.snapElements[u].item.ownerDocument, p.snapElements[u].item) ? (p.snapElements[u].snapping && p.options.snap.release && p.options.snap.release.call(p.element, e, t.extend(p._uiHash(), {
                snapItem: p.snapElements[u].item
            })), p.snapElements[u].snapping = !1) : ("inner" !== g.snapMode && (s = f >= Math.abs(h - b), n = f >= Math.abs(c - v), a = f >= Math.abs(r - _), o = f >= Math.abs(l - m), s && (i.position.top = p._convertPositionTo("relative", {
                top: h - p.helperProportions.height,
                left: 0
            }).top - p.margins.top), n && (i.position.top = p._convertPositionTo("relative", {
                top: c,
                left: 0
            }).top - p.margins.top), a && (i.position.left = p._convertPositionTo("relative", {
                top: 0,
                left: r - p.helperProportions.width
            }).left - p.margins.left), o && (i.position.left = p._convertPositionTo("relative", {
                top: 0,
                left: l
            }).left - p.margins.left)), d = s || n || a || o, "outer" !== g.snapMode && (s = f >= Math.abs(h - v), n = f >= Math.abs(c - b), a = f >= Math.abs(r - m), o = f >= Math.abs(l - _), s && (i.position.top = p._convertPositionTo("relative", {
                top: h,
                left: 0
            }).top - p.margins.top), n && (i.position.top = p._convertPositionTo("relative", {
                top: c - p.helperProportions.height,
                left: 0
            }).top - p.margins.top), a && (i.position.left = p._convertPositionTo("relative", {
                top: 0,
                left: r
            }).left - p.margins.left), o && (i.position.left = p._convertPositionTo("relative", {
                top: 0,
                left: l - p.helperProportions.width
            }).left - p.margins.left)), !p.snapElements[u].snapping && (s || n || a || o || d) && p.options.snap.snap && p.options.snap.snap.call(p.element, e, t.extend(p._uiHash(), {
                snapItem: p.snapElements[u].item
            })), p.snapElements[u].snapping = s || n || a || o || d)
        }
    }), t.ui.plugin.add("draggable", "stack", {
        start: function() {
            var e, i = this.data("ui-draggable").options,
                s = t.makeArray(t(i.stack)).sort(function(e, i) {
                    return (parseInt(t(e).css("zIndex"), 10) || 0) - (parseInt(t(i).css("zIndex"), 10) || 0)
                });
            s.length && (e = parseInt(t(s[0]).css("zIndex"), 10) || 0, t(s).each(function(i) {
                t(this).css("zIndex", e + i)
            }), this.css("zIndex", e + s.length))
        }
    }), t.ui.plugin.add("draggable", "zIndex", {
        start: function(e, i) {
            var s = t(i.helper),
                n = t(this).data("ui-draggable").options;
            s.css("zIndex") && (n._zIndex = s.css("zIndex")), s.css("zIndex", n.zIndex)
        },
        stop: function(e, i) {
            var s = t(this).data("ui-draggable").options;
            s._zIndex && t(i.helper).css("zIndex", s._zIndex)
        }
    })
})(jQuery);
(function(t) {
    function e(t) {
        return parseInt(t, 10) || 0
    }

    function i(t) {
        return !isNaN(parseInt(t, 10))
    }
    t.widget("ui.resizable", t.ui.mouse, {
        version: "1.10.3",
        widgetEventPrefix: "resize",
        options: {
            alsoResize: !1,
            animate: !1,
            animateDuration: "slow",
            animateEasing: "swing",
            aspectRatio: !1,
            autoHide: !1,
            containment: !1,
            ghost: !1,
            grid: !1,
            handles: "e,s,se",
            helper: !1,
            maxHeight: null,
            maxWidth: null,
            minHeight: 10,
            minWidth: 10,
            zIndex: 90,
            resize: null,
            start: null,
            stop: null
        },
        _create: function() {
            var e, i, s, n, a, o = this,
                r = this.options;
            if (this.element.addClass("ui-resizable"), t.extend(this, {
                    _aspectRatio: !!r.aspectRatio,
                    aspectRatio: r.aspectRatio,
                    originalElement: this.element,
                    _proportionallyResizeElements: [],
                    _helper: r.helper || r.ghost || r.animate ? r.helper || "ui-resizable-helper" : null
                }), this.element[0].nodeName.match(/canvas|textarea|input|select|button|img/i) && (this.element.wrap(t("<div class='ui-wrapper' style='overflow: hidden;'></div>").css({
                    position: this.element.css("position"),
                    width: this.element.outerWidth(),
                    height: this.element.outerHeight(),
                    top: this.element.css("top"),
                    left: this.element.css("left")
                })), this.element = this.element.parent().data("ui-resizable", this.element.data("ui-resizable")), this.elementIsWrapper = !0, this.element.css({
                    marginLeft: this.originalElement.css("marginLeft"),
                    marginTop: this.originalElement.css("marginTop"),
                    marginRight: this.originalElement.css("marginRight"),
                    marginBottom: this.originalElement.css("marginBottom")
                }), this.originalElement.css({
                    marginLeft: 0,
                    marginTop: 0,
                    marginRight: 0,
                    marginBottom: 0
                }), this.originalResizeStyle = this.originalElement.css("resize"), this.originalElement.css("resize", "none"), this._proportionallyResizeElements.push(this.originalElement.css({
                    position: "static",
                    zoom: 1,
                    display: "block"
                })), this.originalElement.css({
                    margin: this.originalElement.css("margin")
                }), this._proportionallyResize()), this.handles = r.handles || (t(".ui-resizable-handle", this.element).length ? {
                    n: ".ui-resizable-n",
                    e: ".ui-resizable-e",
                    s: ".ui-resizable-s",
                    w: ".ui-resizable-w",
                    se: ".ui-resizable-se",
                    sw: ".ui-resizable-sw",
                    ne: ".ui-resizable-ne",
                    nw: ".ui-resizable-nw"
                } : "e,s,se"), this.handles.constructor === String)
                for ("all" === this.handles && (this.handles = "n,e,s,w,se,sw,ne,nw"), e = this.handles.split(","), this.handles = {}, i = 0; e.length > i; i++) s = t.trim(e[i]), a = "ui-resizable-" + s, n = t("<div class='ui-resizable-handle " + a + "'></div>"), n.css({
                    zIndex: r.zIndex
                }), "se" === s && n.addClass("ui-icon ui-icon-gripsmall-diagonal-se"), this.handles[s] = ".ui-resizable-" + s, this.element.append(n);
            this._renderAxis = function(e) {
                var i, s, n, a;
                e = e || this.element;
                for (i in this.handles) this.handles[i].constructor === String && (this.handles[i] = t(this.handles[i], this.element).show()), this.elementIsWrapper && this.originalElement[0].nodeName.match(/textarea|input|select|button/i) && (s = t(this.handles[i], this.element), a = /sw|ne|nw|se|n|s/.test(i) ? s.outerHeight() : s.outerWidth(), n = ["padding", /ne|nw|n/.test(i) ? "Top" : /se|sw|s/.test(i) ? "Bottom" : /^e$/.test(i) ? "Right" : "Left"].join(""), e.css(n, a), this._proportionallyResize()), t(this.handles[i]).length
            }, this._renderAxis(this.element), this._handles = t(".ui-resizable-handle", this.element).disableSelection(), this._handles.mouseover(function() {
                o.resizing || (this.className && (n = this.className.match(/ui-resizable-(se|sw|ne|nw|n|e|s|w)/i)), o.axis = n && n[1] ? n[1] : "se")
            }), r.autoHide && (this._handles.hide(), t(this.element).addClass("ui-resizable-autohide").mouseenter(function() {
                r.disabled || (t(this).removeClass("ui-resizable-autohide"), o._handles.show())
            }).mouseleave(function() {
                r.disabled || o.resizing || (t(this).addClass("ui-resizable-autohide"), o._handles.hide())
            })), this._mouseInit()
        },
        _destroy: function() {
            this._mouseDestroy();
            var e, i = function(e) {
                t(e).removeClass("ui-resizable ui-resizable-disabled ui-resizable-resizing").removeData("resizable").removeData("ui-resizable").unbind(".resizable").find(".ui-resizable-handle").remove()
            };
            return this.elementIsWrapper && (i(this.element), e = this.element, this.originalElement.css({
                position: e.css("position"),
                width: e.outerWidth(),
                height: e.outerHeight(),
                top: e.css("top"),
                left: e.css("left")
            }).insertAfter(e), e.remove()), this.originalElement.css("resize", this.originalResizeStyle), i(this.originalElement), this
        },
        _mouseCapture: function(e) {
            var i, s, n = !1;
            for (i in this.handles) s = t(this.handles[i])[0], (s === e.target || t.contains(s, e.target)) && (n = !0);
            return !this.options.disabled && n
        },
        _mouseStart: function(i) {
            var s, n, a, o = this.options,
                r = this.element.position(),
                h = this.element;
            return this.resizing = !0, /absolute/.test(h.css("position")) ? h.css({
                position: "absolute",
                top: h.css("top"),
                left: h.css("left")
            }) : h.is(".ui-draggable") && h.css({
                position: "absolute",
                top: r.top,
                left: r.left
            }), this._renderProxy(), s = e(this.helper.css("left")), n = e(this.helper.css("top")), o.containment && (s += t(o.containment).scrollLeft() || 0, n += t(o.containment).scrollTop() || 0), this.offset = this.helper.offset(), this.position = {
                left: s,
                top: n
            }, this.size = this._helper ? {
                width: h.outerWidth(),
                height: h.outerHeight()
            } : {
                width: h.width(),
                height: h.height()
            }, this.originalSize = this._helper ? {
                width: h.outerWidth(),
                height: h.outerHeight()
            } : {
                width: h.width(),
                height: h.height()
            }, this.originalPosition = {
                left: s,
                top: n
            }, this.sizeDiff = {
                width: h.outerWidth() - h.width(),
                height: h.outerHeight() - h.height()
            }, this.originalMousePosition = {
                left: i.pageX,
                top: i.pageY
            }, this.aspectRatio = "number" == typeof o.aspectRatio ? o.aspectRatio : this.originalSize.width / this.originalSize.height || 1, a = t(".ui-resizable-" + this.axis).css("cursor"), t("body").css("cursor", "auto" === a ? this.axis + "-resize" : a), h.addClass("ui-resizable-resizing"), this._propagate("start", i), !0
        },
        _mouseDrag: function(e) {
            var i, s = this.helper,
                n = {},
                a = this.originalMousePosition,
                o = this.axis,
                r = this.position.top,
                h = this.position.left,
                l = this.size.width,
                c = this.size.height,
                u = e.pageX - a.left || 0,
                d = e.pageY - a.top || 0,
                p = this._change[o];
            return p ? (i = p.apply(this, [e, u, d]), this._updateVirtualBoundaries(e.shiftKey), (this._aspectRatio || e.shiftKey) && (i = this._updateRatio(i, e)), i = this._respectSize(i, e), this._updateCache(i), this._propagate("resize", e), this.position.top !== r && (n.top = this.position.top + "px"), this.position.left !== h && (n.left = this.position.left + "px"), this.size.width !== l && (n.width = this.size.width + "px"), this.size.height !== c && (n.height = this.size.height + "px"), s.css(n), !this._helper && this._proportionallyResizeElements.length && this._proportionallyResize(), t.isEmptyObject(n) || this._trigger("resize", e, this.ui()), !1) : !1
        },
        _mouseStop: function(e) {
            this.resizing = !1;
            var i, s, n, a, o, r, h, l = this.options,
                c = this;
            return this._helper && (i = this._proportionallyResizeElements, s = i.length && /textarea/i.test(i[0].nodeName), n = s && t.ui.hasScroll(i[0], "left") ? 0 : c.sizeDiff.height, a = s ? 0 : c.sizeDiff.width, o = {
                width: c.helper.width() - a,
                height: c.helper.height() - n
            }, r = parseInt(c.element.css("left"), 10) + (c.position.left - c.originalPosition.left) || null, h = parseInt(c.element.css("top"), 10) + (c.position.top - c.originalPosition.top) || null, l.animate || this.element.css(t.extend(o, {
                top: h,
                left: r
            })), c.helper.height(c.size.height), c.helper.width(c.size.width), this._helper && !l.animate && this._proportionallyResize()), t("body").css("cursor", "auto"), this.element.removeClass("ui-resizable-resizing"), this._propagate("stop", e), this._helper && this.helper.remove(), !1
        },
        _updateVirtualBoundaries: function(t) {
            var e, s, n, a, o, r = this.options;
            o = {
                minWidth: i(r.minWidth) ? r.minWidth : 0,
                maxWidth: i(r.maxWidth) ? r.maxWidth : 1 / 0,
                minHeight: i(r.minHeight) ? r.minHeight : 0,
                maxHeight: i(r.maxHeight) ? r.maxHeight : 1 / 0
            }, (this._aspectRatio || t) && (e = o.minHeight * this.aspectRatio, n = o.minWidth / this.aspectRatio, s = o.maxHeight * this.aspectRatio, a = o.maxWidth / this.aspectRatio, e > o.minWidth && (o.minWidth = e), n > o.minHeight && (o.minHeight = n), o.maxWidth > s && (o.maxWidth = s), o.maxHeight > a && (o.maxHeight = a)), this._vBoundaries = o
        },
        _updateCache: function(t) {
            this.offset = this.helper.offset(), i(t.left) && (this.position.left = t.left), i(t.top) && (this.position.top = t.top), i(t.height) && (this.size.height = t.height), i(t.width) && (this.size.width = t.width)
        },
        _updateRatio: function(t) {
            var e = this.position,
                s = this.size,
                n = this.axis;
            return i(t.height) ? t.width = t.height * this.aspectRatio : i(t.width) && (t.height = t.width / this.aspectRatio), "sw" === n && (t.left = e.left + (s.width - t.width), t.top = null), "nw" === n && (t.top = e.top + (s.height - t.height), t.left = e.left + (s.width - t.width)), t
        },
        _respectSize: function(t) {
            var e = this._vBoundaries,
                s = this.axis,
                n = i(t.width) && e.maxWidth && e.maxWidth < t.width,
                a = i(t.height) && e.maxHeight && e.maxHeight < t.height,
                o = i(t.width) && e.minWidth && e.minWidth > t.width,
                r = i(t.height) && e.minHeight && e.minHeight > t.height,
                h = this.originalPosition.left + this.originalSize.width,
                l = this.position.top + this.size.height,
                c = /sw|nw|w/.test(s),
                u = /nw|ne|n/.test(s);
            return o && (t.width = e.minWidth), r && (t.height = e.minHeight), n && (t.width = e.maxWidth), a && (t.height = e.maxHeight), o && c && (t.left = h - e.minWidth), n && c && (t.left = h - e.maxWidth), r && u && (t.top = l - e.minHeight), a && u && (t.top = l - e.maxHeight), t.width || t.height || t.left || !t.top ? t.width || t.height || t.top || !t.left || (t.left = null) : t.top = null, t
        },
        _proportionallyResize: function() {
            if (this._proportionallyResizeElements.length) {
                var t, e, i, s, n, a = this.helper || this.element;
                for (t = 0; this._proportionallyResizeElements.length > t; t++) {
                    if (n = this._proportionallyResizeElements[t], !this.borderDif)
                        for (this.borderDif = [], i = [n.css("borderTopWidth"), n.css("borderRightWidth"), n.css("borderBottomWidth"), n.css("borderLeftWidth")], s = [n.css("paddingTop"), n.css("paddingRight"), n.css("paddingBottom"), n.css("paddingLeft")], e = 0; i.length > e; e++) this.borderDif[e] = (parseInt(i[e], 10) || 0) + (parseInt(s[e], 10) || 0);
                    n.css({
                        height: a.height() - this.borderDif[0] - this.borderDif[2] || 0,
                        width: a.width() - this.borderDif[1] - this.borderDif[3] || 0
                    })
                }
            }
        },
        _renderProxy: function() {
            var e = this.element,
                i = this.options;
            this.elementOffset = e.offset(), this._helper ? (this.helper = this.helper || t("<div style='overflow:hidden;'></div>"), this.helper.addClass(this._helper).css({
                width: this.element.outerWidth() - 1,
                height: this.element.outerHeight() - 1,
                position: "absolute",
                left: this.elementOffset.left + "px",
                top: this.elementOffset.top + "px",
                zIndex: ++i.zIndex
            }), this.helper.appendTo("body").disableSelection()) : this.helper = this.element
        },
        _change: {
            e: function(t, e) {
                return {
                    width: this.originalSize.width + e
                }
            },
            w: function(t, e) {
                var i = this.originalSize,
                    s = this.originalPosition;
                return {
                    left: s.left + e,
                    width: i.width - e
                }
            },
            n: function(t, e, i) {
                var s = this.originalSize,
                    n = this.originalPosition;
                return {
                    top: n.top + i,
                    height: s.height - i
                }
            },
            s: function(t, e, i) {
                return {
                    height: this.originalSize.height + i
                }
            },
            se: function(e, i, s) {
                return t.extend(this._change.s.apply(this, arguments), this._change.e.apply(this, [e, i, s]))
            },
            sw: function(e, i, s) {
                return t.extend(this._change.s.apply(this, arguments), this._change.w.apply(this, [e, i, s]))
            },
            ne: function(e, i, s) {
                return t.extend(this._change.n.apply(this, arguments), this._change.e.apply(this, [e, i, s]))
            },
            nw: function(e, i, s) {
                return t.extend(this._change.n.apply(this, arguments), this._change.w.apply(this, [e, i, s]))
            }
        },
        _propagate: function(e, i) {
            t.ui.plugin.call(this, e, [i, this.ui()]), "resize" !== e && this._trigger(e, i, this.ui())
        },
        plugins: {},
        ui: function() {
            return {
                originalElement: this.originalElement,
                element: this.element,
                helper: this.helper,
                position: this.position,
                size: this.size,
                originalSize: this.originalSize,
                originalPosition: this.originalPosition
            }
        }
    }), t.ui.plugin.add("resizable", "animate", {
        stop: function(e) {
            var i = t(this).data("ui-resizable"),
                s = i.options,
                n = i._proportionallyResizeElements,
                a = n.length && /textarea/i.test(n[0].nodeName),
                o = a && t.ui.hasScroll(n[0], "left") ? 0 : i.sizeDiff.height,
                r = a ? 0 : i.sizeDiff.width,
                h = {
                    width: i.size.width - r,
                    height: i.size.height - o
                },
                l = parseInt(i.element.css("left"), 10) + (i.position.left - i.originalPosition.left) || null,
                c = parseInt(i.element.css("top"), 10) + (i.position.top - i.originalPosition.top) || null;
            i.element.animate(t.extend(h, c && l ? {
                top: c,
                left: l
            } : {}), {
                duration: s.animateDuration,
                easing: s.animateEasing,
                step: function() {
                    var s = {
                        width: parseInt(i.element.css("width"), 10),
                        height: parseInt(i.element.css("height"), 10),
                        top: parseInt(i.element.css("top"), 10),
                        left: parseInt(i.element.css("left"), 10)
                    };
                    n && n.length && t(n[0]).css({
                        width: s.width,
                        height: s.height
                    }), i._updateCache(s), i._propagate("resize", e)
                }
            })
        }
    }), t.ui.plugin.add("resizable", "containment", {
        start: function() {
            var i, s, n, a, o, r, h, l = t(this).data("ui-resizable"),
                c = l.options,
                u = l.element,
                d = c.containment,
                p = d instanceof t ? d.get(0) : /parent/.test(d) ? u.parent().get(0) : d;
            p && (l.containerElement = t(p), /document/.test(d) || d === document ? (l.containerOffset = {
                left: 0,
                top: 0
            }, l.containerPosition = {
                left: 0,
                top: 0
            }, l.parentData = {
                element: t(document),
                left: 0,
                top: 0,
                width: t(document).width(),
                height: t(document).height() || document.body.parentNode.scrollHeight
            }) : (i = t(p), s = [], t(["Top", "Right", "Left", "Bottom"]).each(function(t, n) {
                s[t] = e(i.css("padding" + n))
            }), l.containerOffset = i.offset(), l.containerPosition = i.position(), l.containerSize = {
                height: i.innerHeight() - s[3],
                width: i.innerWidth() - s[1]
            }, n = l.containerOffset, a = l.containerSize.height, o = l.containerSize.width, r = t.ui.hasScroll(p, "left") ? p.scrollWidth : o, h = t.ui.hasScroll(p) ? p.scrollHeight : a, l.parentData = {
                element: p,
                left: n.left,
                top: n.top,
                width: r,
                height: h
            }))
        },
        resize: function(e) {
            var i, s, n, a, o = t(this).data("ui-resizable"),
                r = o.options,
                h = o.containerOffset,
                l = o.position,
                c = o._aspectRatio || e.shiftKey,
                u = {
                    top: 0,
                    left: 0
                },
                d = o.containerElement;
            d[0] !== document && /static/.test(d.css("position")) && (u = h), l.left < (o._helper ? h.left : 0) && (o.size.width = o.size.width + (o._helper ? o.position.left - h.left : o.position.left - u.left), c && (o.size.height = o.size.width / o.aspectRatio), o.position.left = r.helper ? h.left : 0), l.top < (o._helper ? h.top : 0) && (o.size.height = o.size.height + (o._helper ? o.position.top - h.top : o.position.top), c && (o.size.width = o.size.height * o.aspectRatio), o.position.top = o._helper ? h.top : 0), o.offset.left = o.parentData.left + o.position.left, o.offset.top = o.parentData.top + o.position.top, i = Math.abs((o._helper ? o.offset.left - u.left : o.offset.left - u.left) + o.sizeDiff.width), s = Math.abs((o._helper ? o.offset.top - u.top : o.offset.top - h.top) + o.sizeDiff.height), n = o.containerElement.get(0) === o.element.parent().get(0), a = /relative|absolute/.test(o.containerElement.css("position")), n && a && (i -= o.parentData.left), i + o.size.width >= o.parentData.width && (o.size.width = o.parentData.width - i, c && (o.size.height = o.size.width / o.aspectRatio)), s + o.size.height >= o.parentData.height && (o.size.height = o.parentData.height - s, c && (o.size.width = o.size.height * o.aspectRatio))
        },
        stop: function() {
            var e = t(this).data("ui-resizable"),
                i = e.options,
                s = e.containerOffset,
                n = e.containerPosition,
                a = e.containerElement,
                o = t(e.helper),
                r = o.offset(),
                h = o.outerWidth() - e.sizeDiff.width,
                l = o.outerHeight() - e.sizeDiff.height;
            e._helper && !i.animate && /relative/.test(a.css("position")) && t(this).css({
                left: r.left - n.left - s.left,
                width: h,
                height: l
            }), e._helper && !i.animate && /static/.test(a.css("position")) && t(this).css({
                left: r.left - n.left - s.left,
                width: h,
                height: l
            })
        }
    }), t.ui.plugin.add("resizable", "alsoResize", {
        start: function() {
            var e = t(this).data("ui-resizable"),
                i = e.options,
                s = function(e) {
                    t(e).each(function() {
                        var e = t(this);
                        e.data("ui-resizable-alsoresize", {
                            width: parseInt(e.width(), 10),
                            height: parseInt(e.height(), 10),
                            left: parseInt(e.css("left"), 10),
                            top: parseInt(e.css("top"), 10)
                        })
                    })
                };
            "object" != typeof i.alsoResize || i.alsoResize.parentNode ? s(i.alsoResize) : i.alsoResize.length ? (i.alsoResize = i.alsoResize[0], s(i.alsoResize)) : t.each(i.alsoResize, function(t) {
                s(t)
            })
        },
        resize: function(e, i) {
            var s = t(this).data("ui-resizable"),
                n = s.options,
                a = s.originalSize,
                o = s.originalPosition,
                r = {
                    height: s.size.height - a.height || 0,
                    width: s.size.width - a.width || 0,
                    top: s.position.top - o.top || 0,
                    left: s.position.left - o.left || 0
                },
                h = function(e, s) {
                    t(e).each(function() {
                        var e = t(this),
                            n = t(this).data("ui-resizable-alsoresize"),
                            a = {},
                            o = s && s.length ? s : e.parents(i.originalElement[0]).length ? ["width", "height"] : ["width", "height", "top", "left"];
                        t.each(o, function(t, e) {
                            var i = (n[e] || 0) + (r[e] || 0);
                            i && i >= 0 && (a[e] = i || null)
                        }), e.css(a)
                    })
                };
            "object" != typeof n.alsoResize || n.alsoResize.nodeType ? h(n.alsoResize) : t.each(n.alsoResize, function(t, e) {
                h(t, e)
            })
        },
        stop: function() {
            t(this).removeData("resizable-alsoresize")
        }
    }), t.ui.plugin.add("resizable", "ghost", {
        start: function() {
            var e = t(this).data("ui-resizable"),
                i = e.options,
                s = e.size;
            e.ghost = e.originalElement.clone(), e.ghost.css({
                opacity: .25,
                display: "block",
                position: "relative",
                height: s.height,
                width: s.width,
                margin: 0,
                left: 0,
                top: 0
            }).addClass("ui-resizable-ghost").addClass("string" == typeof i.ghost ? i.ghost : ""), e.ghost.appendTo(e.helper)
        },
        resize: function() {
            var e = t(this).data("ui-resizable");
            e.ghost && e.ghost.css({
                position: "relative",
                height: e.size.height,
                width: e.size.width
            })
        },
        stop: function() {
            var e = t(this).data("ui-resizable");
            e.ghost && e.helper && e.helper.get(0).removeChild(e.ghost.get(0))
        }
    }), t.ui.plugin.add("resizable", "grid", {
        resize: function() {
            var e = t(this).data("ui-resizable"),
                i = e.options,
                s = e.size,
                n = e.originalSize,
                a = e.originalPosition,
                o = e.axis,
                r = "number" == typeof i.grid ? [i.grid, i.grid] : i.grid,
                h = r[0] || 1,
                l = r[1] || 1,
                c = Math.round((s.width - n.width) / h) * h,
                u = Math.round((s.height - n.height) / l) * l,
                d = n.width + c,
                p = n.height + u,
                f = i.maxWidth && d > i.maxWidth,
                g = i.maxHeight && p > i.maxHeight,
                m = i.minWidth && i.minWidth > d,
                v = i.minHeight && i.minHeight > p;
            i.grid = r, m && (d += h), v && (p += l), f && (d -= h), g && (p -= l), /^(se|s|e)$/.test(o) ? (e.size.width = d, e.size.height = p) : /^(ne)$/.test(o) ? (e.size.width = d, e.size.height = p, e.position.top = a.top - u) : /^(sw)$/.test(o) ? (e.size.width = d, e.size.height = p, e.position.left = a.left - c) : (e.size.width = d, e.size.height = p, e.position.top = a.top - u, e.position.left = a.left - c)
        }
    })
})(jQuery);

/*!
 * File:        jquery.dataTables.min.js
 * Version:     1.9.4
 * Author:      Allan Jardine (www.sprymedia.co.uk)
 * Info:        www.datatables.net
 *
 * Copyright 2008-2012 Allan Jardine, all rights reserved.
 *
 * This source file is free software, under either the GPL v2 license or a
 * BSD style license, available at:
 *   http://datatables.net/license_gpl2
 *   http://datatables.net/license_bsd
 *
 * This source file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the license files for details.
 */
(function(X, l, n) {
    var L = function(h) {
        var j = function(e) {
            function o(a, b) {
                var c = j.defaults.columns,
                    d = a.aoColumns.length,
                    c = h.extend({}, j.models.oColumn, c, {
                        sSortingClass: a.oClasses.sSortable,
                        sSortingClassJUI: a.oClasses.sSortJUI,
                        nTh: b ? b : l.createElement("th"),
                        sTitle: c.sTitle ? c.sTitle : b ? b.innerHTML : "",
                        aDataSort: c.aDataSort ? c.aDataSort : [d],
                        mData: c.mData ? c.oDefaults : d
                    });
                a.aoColumns.push(c);
                if (a.aoPreSearchCols[d] === n || null === a.aoPreSearchCols[d]) a.aoPreSearchCols[d] = h.extend({}, j.models.oSearch);
                else if (c = a.aoPreSearchCols[d],
                    c.bRegex === n && (c.bRegex = !0), c.bSmart === n && (c.bSmart = !0), c.bCaseInsensitive === n) c.bCaseInsensitive = !0;
                m(a, d, null)
            }

            function m(a, b, c) {
                var d = a.aoColumns[b];
                c !== n && null !== c && (c.mDataProp && !c.mData && (c.mData = c.mDataProp), c.sType !== n && (d.sType = c.sType, d._bAutoType = !1), h.extend(d, c), p(d, c, "sWidth", "sWidthOrig"), c.iDataSort !== n && (d.aDataSort = [c.iDataSort]), p(d, c, "aDataSort"));
                var i = d.mRender ? Q(d.mRender) : null,
                    f = Q(d.mData);
                d.fnGetData = function(a, b) {
                    var c = f(a, b);
                    return d.mRender && b && "" !== b ? i(c, b, a) : c
                };
                d.fnSetData =
                    L(d.mData);
                a.oFeatures.bSort || (d.bSortable = !1);
                !d.bSortable || -1 == h.inArray("asc", d.asSorting) && -1 == h.inArray("desc", d.asSorting) ? (d.sSortingClass = a.oClasses.sSortableNone, d.sSortingClassJUI = "") : -1 == h.inArray("asc", d.asSorting) && -1 == h.inArray("desc", d.asSorting) ? (d.sSortingClass = a.oClasses.sSortable, d.sSortingClassJUI = a.oClasses.sSortJUI) : -1 != h.inArray("asc", d.asSorting) && -1 == h.inArray("desc", d.asSorting) ? (d.sSortingClass = a.oClasses.sSortableAsc, d.sSortingClassJUI = a.oClasses.sSortJUIAscAllowed) : -1 ==
                    h.inArray("asc", d.asSorting) && -1 != h.inArray("desc", d.asSorting) && (d.sSortingClass = a.oClasses.sSortableDesc, d.sSortingClassJUI = a.oClasses.sSortJUIDescAllowed)
            }

            function k(a) {
                if (!1 === a.oFeatures.bAutoWidth) return !1;
                da(a);
                for (var b = 0, c = a.aoColumns.length; b < c; b++) a.aoColumns[b].nTh.style.width = a.aoColumns[b].sWidth
            }

            function G(a, b) {
                var c = r(a, "bVisible");
                return "number" === typeof c[b] ? c[b] : null
            }

            function R(a, b) {
                var c = r(a, "bVisible"),
                    c = h.inArray(b, c);
                return -1 !== c ? c : null
            }

            function t(a) {
                return r(a, "bVisible").length
            }

            function r(a, b) {
                var c = [];
                h.map(a.aoColumns, function(a, i) {
                    a[b] && c.push(i)
                });
                return c
            }

            function B(a) {
                for (var b = j.ext.aTypes, c = b.length, d = 0; d < c; d++) {
                    var i = b[d](a);
                    if (null !== i) return i
                }
                return "string"
            }

            function u(a, b) {
                for (var c = b.split(","), d = [], i = 0, f = a.aoColumns.length; i < f; i++)
                    for (var g = 0; g < f; g++)
                        if (a.aoColumns[i].sName == c[g]) {
                            d.push(g);
                            break
                        }
                return d
            }

            function M(a) {
                for (var b = "", c = 0, d = a.aoColumns.length; c < d; c++) b += a.aoColumns[c].sName + ",";
                return b.length == d ? "" : b.slice(0, -1)
            }

            function ta(a, b, c, d) {
                var i, f,
                    g, e, w;
                if (b)
                    for (i = b.length - 1; 0 <= i; i--) {
                        var j = b[i].aTargets;
                        h.isArray(j) || D(a, 1, "aTargets must be an array of targets, not a " + typeof j);
                        f = 0;
                        for (g = j.length; f < g; f++)
                            if ("number" === typeof j[f] && 0 <= j[f]) {
                                for (; a.aoColumns.length <= j[f];) o(a);
                                d(j[f], b[i])
                            } else if ("number" === typeof j[f] && 0 > j[f]) d(a.aoColumns.length + j[f], b[i]);
                        else if ("string" === typeof j[f]) {
                            e = 0;
                            for (w = a.aoColumns.length; e < w; e++)("_all" == j[f] || h(a.aoColumns[e].nTh).hasClass(j[f])) && d(e, b[i])
                        }
                    }
                if (c) {
                    i = 0;
                    for (a = c.length; i < a; i++) d(i, c[i])
                }
            }

            function H(a,
                b) {
                var c;
                c = h.isArray(b) ? b.slice() : h.extend(!0, {}, b);
                var d = a.aoData.length,
                    i = h.extend(!0, {}, j.models.oRow);
                i._aData = c;
                a.aoData.push(i);
                for (var f, i = 0, g = a.aoColumns.length; i < g; i++) c = a.aoColumns[i], "function" === typeof c.fnRender && c.bUseRendered && null !== c.mData ? F(a, d, i, S(a, d, i)) : F(a, d, i, v(a, d, i)), c._bAutoType && "string" != c.sType && (f = v(a, d, i, "type"), null !== f && "" !== f && (f = B(f), null === c.sType ? c.sType = f : c.sType != f && "html" != c.sType && (c.sType = "string")));
                a.aiDisplayMaster.push(d);
                a.oFeatures.bDeferRender || ea(a,
                    d);
                return d
            }

            function ua(a) {
                var b, c, d, i, f, g, e;
                if (a.bDeferLoading || null === a.sAjaxSource)
                    for (b = a.nTBody.firstChild; b;) {
                        if ("TR" == b.nodeName.toUpperCase()) {
                            c = a.aoData.length;
                            b._DT_RowIndex = c;
                            a.aoData.push(h.extend(!0, {}, j.models.oRow, {
                                nTr: b
                            }));
                            a.aiDisplayMaster.push(c);
                            f = b.firstChild;
                            for (d = 0; f;) {
                                g = f.nodeName.toUpperCase();
                                if ("TD" == g || "TH" == g) F(a, c, d, h.trim(f.innerHTML)), d++;
                                f = f.nextSibling
                            }
                        }
                        b = b.nextSibling
                    }
                i = T(a);
                d = [];
                b = 0;
                for (c = i.length; b < c; b++)
                    for (f = i[b].firstChild; f;) g = f.nodeName.toUpperCase(), ("TD" ==
                        g || "TH" == g) && d.push(f), f = f.nextSibling;
                c = 0;
                for (i = a.aoColumns.length; c < i; c++) {
                    e = a.aoColumns[c];
                    null === e.sTitle && (e.sTitle = e.nTh.innerHTML);
                    var w = e._bAutoType,
                        o = "function" === typeof e.fnRender,
                        k = null !== e.sClass,
                        n = e.bVisible,
                        m, p;
                    if (w || o || k || !n) {
                        g = 0;
                        for (b = a.aoData.length; g < b; g++) f = a.aoData[g], m = d[g * i + c], w && "string" != e.sType && (p = v(a, g, c, "type"), "" !== p && (p = B(p), null === e.sType ? e.sType = p : e.sType != p && "html" != e.sType && (e.sType = "string"))), e.mRender ? m.innerHTML = v(a, g, c, "display") : e.mData !== c && (m.innerHTML = v(a,
                            g, c, "display")), o && (p = S(a, g, c), m.innerHTML = p, e.bUseRendered && F(a, g, c, p)), k && (m.className += " " + e.sClass), n ? f._anHidden[c] = null : (f._anHidden[c] = m, m.parentNode.removeChild(m)), e.fnCreatedCell && e.fnCreatedCell.call(a.oInstance, m, v(a, g, c, "display"), f._aData, g, c)
                    }
                }
                if (0 !== a.aoRowCreatedCallback.length) {
                    b = 0;
                    for (c = a.aoData.length; b < c; b++) f = a.aoData[b], A(a, "aoRowCreatedCallback", null, [f.nTr, f._aData, b])
                }
            }

            function I(a, b) {
                return b._DT_RowIndex !== n ? b._DT_RowIndex : null
            }

            function fa(a, b, c) {
                for (var b = J(a, b), d = 0, a =
                        a.aoColumns.length; d < a; d++)
                    if (b[d] === c) return d;
                return -1
            }

            function Y(a, b, c, d) {
                for (var i = [], f = 0, g = d.length; f < g; f++) i.push(v(a, b, d[f], c));
                return i
            }

            function v(a, b, c, d) {
                var i = a.aoColumns[c];
                if ((c = i.fnGetData(a.aoData[b]._aData, d)) === n) return a.iDrawError != a.iDraw && null === i.sDefaultContent && (D(a, 0, "Requested unknown parameter " + ("function" == typeof i.mData ? "{mData function}" : "'" + i.mData + "'") + " from the data source for row " + b), a.iDrawError = a.iDraw), i.sDefaultContent;
                if (null === c && null !== i.sDefaultContent) c =
                    i.sDefaultContent;
                else if ("function" === typeof c) return c();
                return "display" == d && null === c ? "" : c
            }

            function F(a, b, c, d) {
                a.aoColumns[c].fnSetData(a.aoData[b]._aData, d)
            }

            function Q(a) {
                if (null === a) return function() {
                    return null
                };
                if ("function" === typeof a) return function(b, d, i) {
                    return a(b, d, i)
                };
                if ("string" === typeof a && (-1 !== a.indexOf(".") || -1 !== a.indexOf("["))) {
                    var b = function(a, d, i) {
                        var f = i.split("."),
                            g;
                        if ("" !== i) {
                            var e = 0;
                            for (g = f.length; e < g; e++) {
                                if (i = f[e].match(U)) {
                                    f[e] = f[e].replace(U, "");
                                    "" !== f[e] && (a = a[f[e]]);
                                    g = [];
                                    f.splice(0, e + 1);
                                    for (var f = f.join("."), e = 0, h = a.length; e < h; e++) g.push(b(a[e], d, f));
                                    a = i[0].substring(1, i[0].length - 1);
                                    a = "" === a ? g : g.join(a);
                                    break
                                }
                                if (null === a || a[f[e]] === n) return n;
                                a = a[f[e]]
                            }
                        }
                        return a
                    };
                    return function(c, d) {
                        return b(c, d, a)
                    }
                }
                return function(b) {
                    return b[a]
                }
            }

            function L(a) {
                if (null === a) return function() {};
                if ("function" === typeof a) return function(b, d) {
                    a(b, "set", d)
                };
                if ("string" === typeof a && (-1 !== a.indexOf(".") || -1 !== a.indexOf("["))) {
                    var b = function(a, d, i) {
                        var i = i.split("."),
                            f, g, e = 0;
                        for (g =
                            i.length - 1; e < g; e++) {
                            if (f = i[e].match(U)) {
                                i[e] = i[e].replace(U, "");
                                a[i[e]] = [];
                                f = i.slice();
                                f.splice(0, e + 1);
                                g = f.join(".");
                                for (var h = 0, j = d.length; h < j; h++) f = {}, b(f, d[h], g), a[i[e]].push(f);
                                return
                            }
                            if (null === a[i[e]] || a[i[e]] === n) a[i[e]] = {};
                            a = a[i[e]]
                        }
                        a[i[i.length - 1].replace(U, "")] = d
                    };
                    return function(c, d) {
                        return b(c, d, a)
                    }
                }
                return function(b, d) {
                    b[a] = d
                }
            }

            function Z(a) {
                for (var b = [], c = a.aoData.length, d = 0; d < c; d++) b.push(a.aoData[d]._aData);
                return b
            }

            function ga(a) {
                a.aoData.splice(0, a.aoData.length);
                a.aiDisplayMaster.splice(0,
                    a.aiDisplayMaster.length);
                a.aiDisplay.splice(0, a.aiDisplay.length);
                y(a)
            }

            function ha(a, b) {
                for (var c = -1, d = 0, i = a.length; d < i; d++) a[d] == b ? c = d : a[d] > b && a[d]--; - 1 != c && a.splice(c, 1)
            }

            function S(a, b, c) {
                var d = a.aoColumns[c];
                return d.fnRender({
                    iDataRow: b,
                    iDataColumn: c,
                    oSettings: a,
                    aData: a.aoData[b]._aData,
                    mDataProp: d.mData
                }, v(a, b, c, "display"))
            }

            function ea(a, b) {
                var c = a.aoData[b],
                    d;
                if (null === c.nTr) {
                    c.nTr = l.createElement("tr");
                    c.nTr._DT_RowIndex = b;
                    c._aData.DT_RowId && (c.nTr.id = c._aData.DT_RowId);
                    c._aData.DT_RowClass &&
                        (c.nTr.className = c._aData.DT_RowClass);
                    for (var i = 0, f = a.aoColumns.length; i < f; i++) {
                        var g = a.aoColumns[i];
                        d = l.createElement(g.sCellType);
                        d.innerHTML = "function" === typeof g.fnRender && (!g.bUseRendered || null === g.mData) ? S(a, b, i) : v(a, b, i, "display");
                        null !== g.sClass && (d.className = g.sClass);
                        g.bVisible ? (c.nTr.appendChild(d), c._anHidden[i] = null) : c._anHidden[i] = d;
                        g.fnCreatedCell && g.fnCreatedCell.call(a.oInstance, d, v(a, b, i, "display"), c._aData, b, i)
                    }
                    A(a, "aoRowCreatedCallback", null, [c.nTr, c._aData, b])
                }
            }

            function va(a) {
                var b,
                    c, d;
                if (0 !== h("th, td", a.nTHead).length) {
                    b = 0;
                    for (d = a.aoColumns.length; b < d; b++)
                        if (c = a.aoColumns[b].nTh, c.setAttribute("role", "columnheader"), a.aoColumns[b].bSortable && (c.setAttribute("tabindex", a.iTabIndex), c.setAttribute("aria-controls", a.sTableId)), null !== a.aoColumns[b].sClass && h(c).addClass(a.aoColumns[b].sClass), a.aoColumns[b].sTitle != c.innerHTML) c.innerHTML = a.aoColumns[b].sTitle
                } else {
                    var i = l.createElement("tr");
                    b = 0;
                    for (d = a.aoColumns.length; b < d; b++) c = a.aoColumns[b].nTh, c.innerHTML = a.aoColumns[b].sTitle,
                        c.setAttribute("tabindex", "0"), null !== a.aoColumns[b].sClass && h(c).addClass(a.aoColumns[b].sClass), i.appendChild(c);
                    h(a.nTHead).html("")[0].appendChild(i);
                    V(a.aoHeader, a.nTHead)
                }
                h(a.nTHead).children("tr").attr("role", "row");
                if (a.bJUI) {
                    b = 0;
                    for (d = a.aoColumns.length; b < d; b++) {
                        c = a.aoColumns[b].nTh;
                        i = l.createElement("div");
                        i.className = a.oClasses.sSortJUIWrapper;
                        h(c).contents().appendTo(i);
                        var f = l.createElement("span");
                        f.className = a.oClasses.sSortIcon;
                        i.appendChild(f);
                        c.appendChild(i)
                    }
                }
                if (a.oFeatures.bSort)
                    for (b =
                        0; b < a.aoColumns.length; b++) !1 !== a.aoColumns[b].bSortable ? ia(a, a.aoColumns[b].nTh, b) : h(a.aoColumns[b].nTh).addClass(a.oClasses.sSortableNone);
                "" !== a.oClasses.sFooterTH && h(a.nTFoot).children("tr").children("th").addClass(a.oClasses.sFooterTH);
                if (null !== a.nTFoot) {
                    c = N(a, null, a.aoFooter);
                    b = 0;
                    for (d = a.aoColumns.length; b < d; b++) c[b] && (a.aoColumns[b].nTf = c[b], a.aoColumns[b].sClass && h(c[b]).addClass(a.aoColumns[b].sClass))
                }
            }

            function W(a, b, c) {
                var d, i, f, g = [],
                    e = [],
                    h = a.aoColumns.length,
                    j;
                c === n && (c = !1);
                d = 0;
                for (i =
                    b.length; d < i; d++) {
                    g[d] = b[d].slice();
                    g[d].nTr = b[d].nTr;
                    for (f = h - 1; 0 <= f; f--) !a.aoColumns[f].bVisible && !c && g[d].splice(f, 1);
                    e.push([])
                }
                d = 0;
                for (i = g.length; d < i; d++) {
                    if (a = g[d].nTr)
                        for (; f = a.firstChild;) a.removeChild(f);
                    f = 0;
                    for (b = g[d].length; f < b; f++)
                        if (j = h = 1, e[d][f] === n) {
                            a.appendChild(g[d][f].cell);
                            for (e[d][f] = 1; g[d + h] !== n && g[d][f].cell == g[d + h][f].cell;) e[d + h][f] = 1, h++;
                            for (; g[d][f + j] !== n && g[d][f].cell == g[d][f + j].cell;) {
                                for (c = 0; c < h; c++) e[d + c][f + j] = 1;
                                j++
                            }
                            g[d][f].cell.rowSpan = h;
                            g[d][f].cell.colSpan = j
                        }
                }
            }

            function x(a) {
                var b =
                    A(a, "aoPreDrawCallback", "preDraw", [a]);
                if (-1 !== h.inArray(!1, b)) E(a, !1);
                else {
                    var c, d, b = [],
                        i = 0,
                        f = a.asStripeClasses.length;
                    c = a.aoOpenRows.length;
                    a.bDrawing = !0;
                    a.iInitDisplayStart !== n && -1 != a.iInitDisplayStart && (a._iDisplayStart = a.oFeatures.bServerSide ? a.iInitDisplayStart : a.iInitDisplayStart >= a.fnRecordsDisplay() ? 0 : a.iInitDisplayStart, a.iInitDisplayStart = -1, y(a));
                    if (a.bDeferLoading) a.bDeferLoading = !1, a.iDraw++;
                    else if (a.oFeatures.bServerSide) {
                        if (!a.bDestroying && !wa(a)) return
                    } else a.iDraw++;
                    if (0 !== a.aiDisplay.length) {
                        var g =
                            a._iDisplayStart;
                        d = a._iDisplayEnd;
                        a.oFeatures.bServerSide && (g = 0, d = a.aoData.length);
                        for (; g < d; g++) {
                            var e = a.aoData[a.aiDisplay[g]];
                            null === e.nTr && ea(a, a.aiDisplay[g]);
                            var j = e.nTr;
                            if (0 !== f) {
                                var o = a.asStripeClasses[i % f];
                                e._sRowStripe != o && (h(j).removeClass(e._sRowStripe).addClass(o), e._sRowStripe = o)
                            }
                            A(a, "aoRowCallback", null, [j, a.aoData[a.aiDisplay[g]]._aData, i, g]);
                            b.push(j);
                            i++;
                            if (0 !== c)
                                for (e = 0; e < c; e++)
                                    if (j == a.aoOpenRows[e].nParent) {
                                        b.push(a.aoOpenRows[e].nTr);
                                        break
                                    }
                        }
                    } else b[0] = l.createElement("tr"), a.asStripeClasses[0] &&
                        (b[0].className = a.asStripeClasses[0]), c = a.oLanguage, f = c.sZeroRecords, 1 == a.iDraw && null !== a.sAjaxSource && !a.oFeatures.bServerSide ? f = c.sLoadingRecords : c.sEmptyTable && 0 === a.fnRecordsTotal() && (f = c.sEmptyTable), c = l.createElement("td"), c.setAttribute("valign", "top"), c.colSpan = t(a), c.className = a.oClasses.sRowEmpty, c.innerHTML = ja(a, f), b[i].appendChild(c);
                    A(a, "aoHeaderCallback", "header", [h(a.nTHead).children("tr")[0], Z(a), a._iDisplayStart, a.fnDisplayEnd(), a.aiDisplay]);
                    A(a, "aoFooterCallback", "footer", [h(a.nTFoot).children("tr")[0],
                        Z(a), a._iDisplayStart, a.fnDisplayEnd(), a.aiDisplay
                    ]);
                    i = l.createDocumentFragment();
                    c = l.createDocumentFragment();
                    if (a.nTBody) {
                        f = a.nTBody.parentNode;
                        c.appendChild(a.nTBody);
                        if (!a.oScroll.bInfinite || !a._bInitComplete || a.bSorted || a.bFiltered)
                            for (; c = a.nTBody.firstChild;) a.nTBody.removeChild(c);
                        c = 0;
                        for (d = b.length; c < d; c++) i.appendChild(b[c]);
                        a.nTBody.appendChild(i);
                        null !== f && f.appendChild(a.nTBody)
                    }
                    A(a, "aoDrawCallback", "draw", [a]);
                    a.bSorted = !1;
                    a.bFiltered = !1;
                    a.bDrawing = !1;
                    a.oFeatures.bServerSide && (E(a, !1),
                        a._bInitComplete || $(a))
                }
            }

            function aa(a) {
                a.oFeatures.bSort ? O(a, a.oPreviousSearch) : a.oFeatures.bFilter ? K(a, a.oPreviousSearch) : (y(a), x(a))
            }

            function xa(a) {
                var b = h("<div></div>")[0];
                a.nTable.parentNode.insertBefore(b, a.nTable);
                a.nTableWrapper = h('<div id="' + a.sTableId + '_wrapper" class="' + a.oClasses.sWrapper + '" role="grid"></div>')[0];
                a.nTableReinsertBefore = a.nTable.nextSibling;
                for (var c = a.nTableWrapper, d = a.sDom.split(""), i, f, g, e, w, o, k, m = 0; m < d.length; m++) {
                    f = 0;
                    g = d[m];
                    if ("<" == g) {
                        e = h("<div></div>")[0];
                        w = d[m +
                            1];
                        if ("'" == w || '"' == w) {
                            o = "";
                            for (k = 2; d[m + k] != w;) o += d[m + k], k++;
                            "H" == o ? o = a.oClasses.sJUIHeader : "F" == o && (o = a.oClasses.sJUIFooter); - 1 != o.indexOf(".") ? (w = o.split("."), e.id = w[0].substr(1, w[0].length - 1), e.className = w[1]) : "#" == o.charAt(0) ? e.id = o.substr(1, o.length - 1) : e.className = o;
                            m += k
                        }
                        c.appendChild(e);
                        c = e
                    } else if (">" == g) c = c.parentNode;
                    else if ("l" == g && a.oFeatures.bPaginate && a.oFeatures.bLengthChange) i = ya(a), f = 1;
                    else if ("f" == g && a.oFeatures.bFilter) i = za(a), f = 1;
                    else if ("r" == g && a.oFeatures.bProcessing) i = Aa(a), f =
                        1;
                    else if ("t" == g) i = Ba(a), f = 1;
                    else if ("i" == g && a.oFeatures.bInfo) i = Ca(a), f = 1;
                    else if ("p" == g && a.oFeatures.bPaginate) i = Da(a), f = 1;
                    else if (0 !== j.ext.aoFeatures.length) {
                        e = j.ext.aoFeatures;
                        k = 0;
                        for (w = e.length; k < w; k++)
                            if (g == e[k].cFeature) {
                                (i = e[k].fnInit(a)) && (f = 1);
                                break
                            }
                    }
                    1 == f && null !== i && ("object" !== typeof a.aanFeatures[g] && (a.aanFeatures[g] = []), a.aanFeatures[g].push(i), c.appendChild(i))
                }
                b.parentNode.replaceChild(a.nTableWrapper, b)
            }

            function V(a, b) {
                var c = h(b).children("tr"),
                    d, i, f, g, e, j, o, k, m, p;
                a.splice(0, a.length);
                f = 0;
                for (j = c.length; f < j; f++) a.push([]);
                f = 0;
                for (j = c.length; f < j; f++) {
                    d = c[f];
                    for (i = d.firstChild; i;) {
                        if ("TD" == i.nodeName.toUpperCase() || "TH" == i.nodeName.toUpperCase()) {
                            k = 1 * i.getAttribute("colspan");
                            m = 1 * i.getAttribute("rowspan");
                            k = !k || 0 === k || 1 === k ? 1 : k;
                            m = !m || 0 === m || 1 === m ? 1 : m;
                            g = 0;
                            for (e = a[f]; e[g];) g++;
                            o = g;
                            p = 1 === k ? !0 : !1;
                            for (e = 0; e < k; e++)
                                for (g = 0; g < m; g++) a[f + g][o + e] = {
                                    cell: i,
                                    unique: p
                                }, a[f + g].nTr = d
                        }
                        i = i.nextSibling
                    }
                }
            }

            function N(a, b, c) {
                var d = [];
                c || (c = a.aoHeader, b && (c = [], V(c, b)));
                for (var b = 0, i = c.length; b < i; b++)
                    for (var f =
                            0, g = c[b].length; f < g; f++)
                        if (c[b][f].unique && (!d[f] || !a.bSortCellsTop)) d[f] = c[b][f].cell;
                return d
            }

            function wa(a) {
                if (a.bAjaxDataGet) {
                    a.iDraw++;
                    E(a, !0);
                    var b = Ea(a);
                    ka(a, b);
                    a.fnServerData.call(a.oInstance, a.sAjaxSource, b, function(b) {
                        Fa(a, b)
                    }, a);
                    return !1
                }
                return !0
            }

            function Ea(a) {
                var b = a.aoColumns.length,
                    c = [],
                    d, i, f, g;
                c.push({
                    name: "sEcho",
                    value: a.iDraw
                });
                c.push({
                    name: "iColumns",
                    value: b
                });
                c.push({
                    name: "sColumns",
                    value: M(a)
                });
                c.push({
                    name: "iDisplayStart",
                    value: a._iDisplayStart
                });
                c.push({
                    name: "iDisplayLength",
                    value: !1 !== a.oFeatures.bPaginate ? a._iDisplayLength : -1
                });
                for (f = 0; f < b; f++) d = a.aoColumns[f].mData, c.push({
                    name: "mDataProp_" + f,
                    value: "function" === typeof d ? "function" : d
                });
                if (!1 !== a.oFeatures.bFilter) {
                    c.push({
                        name: "sSearch",
                        value: a.oPreviousSearch.sSearch
                    });
                    c.push({
                        name: "bRegex",
                        value: a.oPreviousSearch.bRegex
                    });
                    for (f = 0; f < b; f++) c.push({
                        name: "sSearch_" + f,
                        value: a.aoPreSearchCols[f].sSearch
                    }), c.push({
                        name: "bRegex_" + f,
                        value: a.aoPreSearchCols[f].bRegex
                    }), c.push({
                        name: "bSearchable_" + f,
                        value: a.aoColumns[f].bSearchable
                    })
                }
                if (!1 !==
                    a.oFeatures.bSort) {
                    var e = 0;
                    d = null !== a.aaSortingFixed ? a.aaSortingFixed.concat(a.aaSorting) : a.aaSorting.slice();
                    for (f = 0; f < d.length; f++) {
                        i = a.aoColumns[d[f][0]].aDataSort;
                        for (g = 0; g < i.length; g++) c.push({
                            name: "iSortCol_" + e,
                            value: i[g]
                        }), c.push({
                            name: "sSortDir_" + e,
                            value: d[f][1]
                        }), e++
                    }
                    c.push({
                        name: "iSortingCols",
                        value: e
                    });
                    for (f = 0; f < b; f++) c.push({
                        name: "bSortable_" + f,
                        value: a.aoColumns[f].bSortable
                    })
                }
                return c
            }

            function ka(a, b) {
                A(a, "aoServerParams", "serverParams", [b])
            }

            function Fa(a, b) {
                if (b.sEcho !== n) {
                    if (1 * b.sEcho <
                        a.iDraw) return;
                    a.iDraw = 1 * b.sEcho
                }(!a.oScroll.bInfinite || a.oScroll.bInfinite && (a.bSorted || a.bFiltered)) && ga(a);
                a._iRecordsTotal = parseInt(b.iTotalRecords, 10);
                a._iRecordsDisplay = parseInt(b.iTotalDisplayRecords, 10);
                var c = M(a),
                    c = b.sColumns !== n && "" !== c && b.sColumns != c,
                    d;
                c && (d = u(a, b.sColumns));
                for (var i = Q(a.sAjaxDataProp)(b), f = 0, g = i.length; f < g; f++)
                    if (c) {
                        for (var e = [], h = 0, j = a.aoColumns.length; h < j; h++) e.push(i[f][d[h]]);
                        H(a, e)
                    } else H(a, i[f]);
                a.aiDisplay = a.aiDisplayMaster.slice();
                a.bAjaxDataGet = !1;
                x(a);
                a.bAjaxDataGet = !0;
                E(a, !1)
            }

            function za(a) {
                var b = a.oPreviousSearch,
                    c = a.oLanguage.sSearch,
                    c = -1 !== c.indexOf("_INPUT_") ? c.replace("_INPUT_", '<input type="text" />') : "" === c ? '<input type="text" />' : c + ' <input type="text" />',
                    d = l.createElement("div");
                d.className = a.oClasses.sFilter;
                d.innerHTML = "<label>" + c + "</label>";
                a.aanFeatures.f || (d.id = a.sTableId + "_filter");
                c = h('input[type="text"]', d);
                d._DT_Input = c[0];
                c.val(b.sSearch.replace('"', "&quot;"));
                c.bind("keyup.DT", function() {
                    for (var c = a.aanFeatures.f, d = this.value === "" ? "" : this.value,
                            g = 0, e = c.length; g < e; g++) c[g] != h(this).parents("div.dataTables_filter")[0] && h(c[g]._DT_Input).val(d);
                    d != b.sSearch && K(a, {
                        sSearch: d,
                        bRegex: b.bRegex,
                        bSmart: b.bSmart,
                        bCaseInsensitive: b.bCaseInsensitive
                    })
                });
                c.attr("aria-controls", a.sTableId).bind("keypress.DT", function(a) {
                    if (a.keyCode == 13) return false
                });
                return d
            }

            function K(a, b, c) {
                var d = a.oPreviousSearch,
                    i = a.aoPreSearchCols,
                    f = function(a) {
                        d.sSearch = a.sSearch;
                        d.bRegex = a.bRegex;
                        d.bSmart = a.bSmart;
                        d.bCaseInsensitive = a.bCaseInsensitive
                    };
                if (a.oFeatures.bServerSide) f(b);
                else {
                    Ga(a, b.sSearch, c, b.bRegex, b.bSmart, b.bCaseInsensitive);
                    f(b);
                    for (b = 0; b < a.aoPreSearchCols.length; b++) Ha(a, i[b].sSearch, b, i[b].bRegex, i[b].bSmart, i[b].bCaseInsensitive);
                    Ia(a)
                }
                a.bFiltered = !0;
                h(a.oInstance).trigger("filter", a);
                a._iDisplayStart = 0;
                y(a);
                x(a);
                la(a, 0)
            }

            function Ia(a) {
                for (var b = j.ext.afnFiltering, c = r(a, "bSearchable"), d = 0, i = b.length; d < i; d++)
                    for (var f = 0, g = 0, e = a.aiDisplay.length; g < e; g++) {
                        var h = a.aiDisplay[g - f];
                        b[d](a, Y(a, h, "filter", c), h) || (a.aiDisplay.splice(g - f, 1), f++)
                    }
            }

            function Ha(a, b, c,
                d, i, f) {
                if ("" !== b)
                    for (var g = 0, b = ma(b, d, i, f), d = a.aiDisplay.length - 1; 0 <= d; d--) i = Ja(v(a, a.aiDisplay[d], c, "filter"), a.aoColumns[c].sType), b.test(i) || (a.aiDisplay.splice(d, 1), g++)
            }

            function Ga(a, b, c, d, i, f) {
                d = ma(b, d, i, f);
                i = a.oPreviousSearch;
                c || (c = 0);
                0 !== j.ext.afnFiltering.length && (c = 1);
                if (0 >= b.length) a.aiDisplay.splice(0, a.aiDisplay.length), a.aiDisplay = a.aiDisplayMaster.slice();
                else if (a.aiDisplay.length == a.aiDisplayMaster.length || i.sSearch.length > b.length || 1 == c || 0 !== b.indexOf(i.sSearch)) {
                    a.aiDisplay.splice(0,
                        a.aiDisplay.length);
                    la(a, 1);
                    for (b = 0; b < a.aiDisplayMaster.length; b++) d.test(a.asDataSearch[b]) && a.aiDisplay.push(a.aiDisplayMaster[b])
                } else
                    for (b = c = 0; b < a.asDataSearch.length; b++) d.test(a.asDataSearch[b]) || (a.aiDisplay.splice(b - c, 1), c++)
            }

            function la(a, b) {
                if (!a.oFeatures.bServerSide) {
                    a.asDataSearch = [];
                    for (var c = r(a, "bSearchable"), d = 1 === b ? a.aiDisplayMaster : a.aiDisplay, i = 0, f = d.length; i < f; i++) a.asDataSearch[i] = na(a, Y(a, d[i], "filter", c))
                }
            }

            function na(a, b) {
                var c = b.join("  "); - 1 !== c.indexOf("&") && (c = h("<div>").html(c).text());
                return c.replace(/[\n\r]/g, " ")
            }

            function ma(a, b, c, d) {
                if (c) return a = b ? a.split(" ") : oa(a).split(" "), a = "^(?=.*?" + a.join(")(?=.*?") + ").*$", RegExp(a, d ? "i" : "");
                a = b ? a : oa(a);
                return RegExp(a, d ? "i" : "")
            }

            function Ja(a, b) {
                return "function" === typeof j.ext.ofnSearch[b] ? j.ext.ofnSearch[b](a) : null === a ? "" : "html" == b ? a.replace(/[\r\n]/g, " ").replace(/<.*?>/g, "") : "string" === typeof a ? a.replace(/[\r\n]/g, " ") : a
            }

            function oa(a) {
                return a.replace(RegExp("(\\/|\\.|\\*|\\+|\\?|\\||\\(|\\)|\\[|\\]|\\{|\\}|\\\\|\\$|\\^|\\-)", "g"),
                    "\\$1")
            }

            function Ca(a) {
                var b = l.createElement("div");
                b.className = a.oClasses.sInfo;
                a.aanFeatures.i || (a.aoDrawCallback.push({
                    fn: Ka,
                    sName: "information"
                }), b.id = a.sTableId + "_info");
                a.nTable.setAttribute("aria-describedby", a.sTableId + "_info");
                return b
            }

            function Ka(a) {
                if (a.oFeatures.bInfo && 0 !== a.aanFeatures.i.length) {
                    var b = a.oLanguage,
                        c = a._iDisplayStart + 1,
                        d = a.fnDisplayEnd(),
                        i = a.fnRecordsTotal(),
                        f = a.fnRecordsDisplay(),
                        g;
                    g = 0 === f ? b.sInfoEmpty : b.sInfo;
                    f != i && (g += " " + b.sInfoFiltered);
                    g += b.sInfoPostFix;
                    g = ja(a, g);
                    null !== b.fnInfoCallback && (g = b.fnInfoCallback.call(a.oInstance, a, c, d, i, f, g));
                    a = a.aanFeatures.i;
                    b = 0;
                    for (c = a.length; b < c; b++) h(a[b]).html(g)
                }
            }

            function ja(a, b) {
                var c = a.fnFormatNumber(a._iDisplayStart + 1),
                    d = a.fnDisplayEnd(),
                    d = a.fnFormatNumber(d),
                    i = a.fnRecordsDisplay(),
                    i = a.fnFormatNumber(i),
                    f = a.fnRecordsTotal(),
                    f = a.fnFormatNumber(f);
                a.oScroll.bInfinite && (c = a.fnFormatNumber(1));
                return b.replace(/_START_/g, c).replace(/_END_/g, d).replace(/_TOTAL_/g, i).replace(/_MAX_/g, f)
            }

            function ba(a) {
                var b, c, d = a.iInitDisplayStart;
                if (!1 === a.bInitialised) setTimeout(function() {
                    ba(a)
                }, 200);
                else {
                    xa(a);
                    va(a);
                    W(a, a.aoHeader);
                    a.nTFoot && W(a, a.aoFooter);
                    E(a, !0);
                    a.oFeatures.bAutoWidth && da(a);
                    b = 0;
                    for (c = a.aoColumns.length; b < c; b++) null !== a.aoColumns[b].sWidth && (a.aoColumns[b].nTh.style.width = q(a.aoColumns[b].sWidth));
                    a.oFeatures.bSort ? O(a) : a.oFeatures.bFilter ? K(a, a.oPreviousSearch) : (a.aiDisplay = a.aiDisplayMaster.slice(), y(a), x(a));
                    null !== a.sAjaxSource && !a.oFeatures.bServerSide ? (c = [], ka(a, c), a.fnServerData.call(a.oInstance, a.sAjaxSource,
                        c,
                        function(c) {
                            var f = a.sAjaxDataProp !== "" ? Q(a.sAjaxDataProp)(c) : c;
                            for (b = 0; b < f.length; b++) H(a, f[b]);
                            a.iInitDisplayStart = d;
                            if (a.oFeatures.bSort) O(a);
                            else {
                                a.aiDisplay = a.aiDisplayMaster.slice();
                                y(a);
                                x(a)
                            }
                            E(a, false);
                            $(a, c)
                        }, a)) : a.oFeatures.bServerSide || (E(a, !1), $(a))
                }
            }

            function $(a, b) {
                a._bInitComplete = !0;
                A(a, "aoInitComplete", "init", [a, b])
            }

            function pa(a) {
                var b = j.defaults.oLanguage;
                !a.sEmptyTable && (a.sZeroRecords && "No data available in table" === b.sEmptyTable) && p(a, a, "sZeroRecords", "sEmptyTable");
                !a.sLoadingRecords &&
                    (a.sZeroRecords && "Loading..." === b.sLoadingRecords) && p(a, a, "sZeroRecords", "sLoadingRecords")
            }

            function ya(a) {
                if (a.oScroll.bInfinite) return null;
                var b = '<select size="1" ' + ('name="' + a.sTableId + '_length"') + ">",
                    c, d, i = a.aLengthMenu;
                if (2 == i.length && "object" === typeof i[0] && "object" === typeof i[1]) {
                    c = 0;
                    for (d = i[0].length; c < d; c++) b += '<option value="' + i[0][c] + '">' + i[1][c] + "</option>"
                } else {
                    c = 0;
                    for (d = i.length; c < d; c++) b += '<option value="' + i[c] + '">' + i[c] + "</option>"
                }
                b += "</select>";
                i = l.createElement("div");
                a.aanFeatures.l ||
                    (i.id = a.sTableId + "_length");
                i.className = a.oClasses.sLength;
                i.innerHTML = "<label>" + a.oLanguage.sLengthMenu.replace("_MENU_", b) + "</label>";
                h('select option[value="' + a._iDisplayLength + '"]', i).attr("selected", !0);
                h("select", i).bind("change.DT", function() {
                    var b = h(this).val(),
                        i = a.aanFeatures.l;
                    c = 0;
                    for (d = i.length; c < d; c++) i[c] != this.parentNode && h("select", i[c]).val(b);
                    a._iDisplayLength = parseInt(b, 10);
                    y(a);
                    if (a.fnDisplayEnd() == a.fnRecordsDisplay()) {
                        a._iDisplayStart = a.fnDisplayEnd() - a._iDisplayLength;
                        if (a._iDisplayStart <
                            0) a._iDisplayStart = 0
                    }
                    if (a._iDisplayLength == -1) a._iDisplayStart = 0;
                    x(a)
                });
                h("select", i).attr("aria-controls", a.sTableId);
                return i
            }

            function y(a) {
                a._iDisplayEnd = !1 === a.oFeatures.bPaginate ? a.aiDisplay.length : a._iDisplayStart + a._iDisplayLength > a.aiDisplay.length || -1 == a._iDisplayLength ? a.aiDisplay.length : a._iDisplayStart + a._iDisplayLength
            }

            function Da(a) {
                if (a.oScroll.bInfinite) return null;
                var b = l.createElement("div");
                b.className = a.oClasses.sPaging + a.sPaginationType;
                j.ext.oPagination[a.sPaginationType].fnInit(a,
                    b,
                    function(a) {
                        y(a);
                        x(a)
                    });
                a.aanFeatures.p || a.aoDrawCallback.push({
                    fn: function(a) {
                        j.ext.oPagination[a.sPaginationType].fnUpdate(a, function(a) {
                            y(a);
                            x(a)
                        })
                    },
                    sName: "pagination"
                });
                return b
            }

            function qa(a, b) {
                var c = a._iDisplayStart;
                if ("number" === typeof b) a._iDisplayStart = b * a._iDisplayLength, a._iDisplayStart > a.fnRecordsDisplay() && (a._iDisplayStart = 0);
                else if ("first" == b) a._iDisplayStart = 0;
                else if ("previous" == b) a._iDisplayStart = 0 <= a._iDisplayLength ? a._iDisplayStart - a._iDisplayLength : 0, 0 > a._iDisplayStart && (a._iDisplayStart =
                    0);
                else if ("next" == b) 0 <= a._iDisplayLength ? a._iDisplayStart + a._iDisplayLength < a.fnRecordsDisplay() && (a._iDisplayStart += a._iDisplayLength) : a._iDisplayStart = 0;
                else if ("last" == b)
                    if (0 <= a._iDisplayLength) {
                        var d = parseInt((a.fnRecordsDisplay() - 1) / a._iDisplayLength, 10) + 1;
                        a._iDisplayStart = (d - 1) * a._iDisplayLength
                    } else a._iDisplayStart = 0;
                else D(a, 0, "Unknown paging action: " + b);
                h(a.oInstance).trigger("page", a);
                return c != a._iDisplayStart
            }

            function Aa(a) {
                var b = l.createElement("div");
                a.aanFeatures.r || (b.id = a.sTableId +
                    "_processing");
                b.innerHTML = a.oLanguage.sProcessing;
                b.className = a.oClasses.sProcessing;
                a.nTable.parentNode.insertBefore(b, a.nTable);
                return b
            }

            function E(a, b) {
                if (a.oFeatures.bProcessing)
                    for (var c = a.aanFeatures.r, d = 0, i = c.length; d < i; d++) c[d].style.visibility = b ? "visible" : "hidden";
                h(a.oInstance).trigger("processing", [a, b])
            }

            function Ba(a) {
                if ("" === a.oScroll.sX && "" === a.oScroll.sY) return a.nTable;
                var b = l.createElement("div"),
                    c = l.createElement("div"),
                    d = l.createElement("div"),
                    i = l.createElement("div"),
                    f = l.createElement("div"),
                    g = l.createElement("div"),
                    e = a.nTable.cloneNode(!1),
                    j = a.nTable.cloneNode(!1),
                    o = a.nTable.getElementsByTagName("thead")[0],
                    k = 0 === a.nTable.getElementsByTagName("tfoot").length ? null : a.nTable.getElementsByTagName("tfoot")[0],
                    m = a.oClasses;
                c.appendChild(d);
                f.appendChild(g);
                i.appendChild(a.nTable);
                b.appendChild(c);
                b.appendChild(i);
                d.appendChild(e);
                e.appendChild(o);
                null !== k && (b.appendChild(f), g.appendChild(j), j.appendChild(k));
                b.className = m.sScrollWrapper;
                c.className = m.sScrollHead;
                d.className = m.sScrollHeadInner;
                i.className = m.sScrollBody;
                f.className = m.sScrollFoot;
                g.className = m.sScrollFootInner;
                a.oScroll.bAutoCss && (c.style.overflow = "hidden", c.style.position = "relative", f.style.overflow = "hidden", i.style.overflow = "auto");
                c.style.border = "0";
                c.style.width = "100%";
                f.style.border = "0";
                d.style.width = "" !== a.oScroll.sXInner ? a.oScroll.sXInner : "100%";
                e.removeAttribute("id");
                e.style.marginLeft = "0";
                a.nTable.style.marginLeft = "0";
                null !== k && (j.removeAttribute("id"), j.style.marginLeft = "0");
                d = h(a.nTable).children("caption");
                0 <
                    d.length && (d = d[0], "top" === d._captionSide ? e.appendChild(d) : "bottom" === d._captionSide && k && j.appendChild(d));
                "" !== a.oScroll.sX && (c.style.width = q(a.oScroll.sX), i.style.width = q(a.oScroll.sX), null !== k && (f.style.width = q(a.oScroll.sX)), h(i).scroll(function() {
                    c.scrollLeft = this.scrollLeft;
                    if (k !== null) f.scrollLeft = this.scrollLeft
                }));
                "" !== a.oScroll.sY && (i.style.height = q(a.oScroll.sY));
                a.aoDrawCallback.push({
                    fn: La,
                    sName: "scrolling"
                });
                a.oScroll.bInfinite && h(i).scroll(function() {
                    if (!a.bDrawing && h(this).scrollTop() !==
                        0 && h(this).scrollTop() + h(this).height() > h(a.nTable).height() - a.oScroll.iLoadGap && a.fnDisplayEnd() < a.fnRecordsDisplay()) {
                        qa(a, "next");
                        y(a);
                        x(a)
                    }
                });
                a.nScrollHead = c;
                a.nScrollFoot = f;
                return b
            }

            function La(a) {
                var b = a.nScrollHead.getElementsByTagName("div")[0],
                    c = b.getElementsByTagName("table")[0],
                    d = a.nTable.parentNode,
                    i, f, g, e, j, o, k, m, p = [],
                    n = [],
                    l = null !== a.nTFoot ? a.nScrollFoot.getElementsByTagName("div")[0] : null,
                    R = null !== a.nTFoot ? l.getElementsByTagName("table")[0] : null,
                    r = a.oBrowser.bScrollOversize,
                    s = function(a) {
                        k =
                            a.style;
                        k.paddingTop = "0";
                        k.paddingBottom = "0";
                        k.borderTopWidth = "0";
                        k.borderBottomWidth = "0";
                        k.height = 0
                    };
                h(a.nTable).children("thead, tfoot").remove();
                i = h(a.nTHead).clone()[0];
                a.nTable.insertBefore(i, a.nTable.childNodes[0]);
                g = a.nTHead.getElementsByTagName("tr");
                e = i.getElementsByTagName("tr");
                null !== a.nTFoot && (j = h(a.nTFoot).clone()[0], a.nTable.insertBefore(j, a.nTable.childNodes[1]), o = a.nTFoot.getElementsByTagName("tr"), j = j.getElementsByTagName("tr"));
                "" === a.oScroll.sX && (d.style.width = "100%", b.parentNode.style.width =
                    "100%");
                var t = N(a, i);
                i = 0;
                for (f = t.length; i < f; i++) m = G(a, i), t[i].style.width = a.aoColumns[m].sWidth;
                null !== a.nTFoot && C(function(a) {
                    a.style.width = ""
                }, j);
                a.oScroll.bCollapse && "" !== a.oScroll.sY && (d.style.height = d.offsetHeight + a.nTHead.offsetHeight + "px");
                i = h(a.nTable).outerWidth();
                if ("" === a.oScroll.sX) {
                    if (a.nTable.style.width = "100%", r && (h("tbody", d).height() > d.offsetHeight || "scroll" == h(d).css("overflow-y"))) a.nTable.style.width = q(h(a.nTable).outerWidth() - a.oScroll.iBarWidth)
                } else "" !== a.oScroll.sXInner ? a.nTable.style.width =
                    q(a.oScroll.sXInner) : i == h(d).width() && h(d).height() < h(a.nTable).height() ? (a.nTable.style.width = q(i - a.oScroll.iBarWidth), h(a.nTable).outerWidth() > i - a.oScroll.iBarWidth && (a.nTable.style.width = q(i))) : a.nTable.style.width = q(i);
                i = h(a.nTable).outerWidth();
                C(s, e);
                C(function(a) {
                    p.push(q(h(a).width()))
                }, e);
                C(function(a, b) {
                    a.style.width = p[b]
                }, g);
                h(e).height(0);
                null !== a.nTFoot && (C(s, j), C(function(a) {
                    n.push(q(h(a).width()))
                }, j), C(function(a, b) {
                    a.style.width = n[b]
                }, o), h(j).height(0));
                C(function(a, b) {
                    a.innerHTML =
                        "";
                    a.style.width = p[b]
                }, e);
                null !== a.nTFoot && C(function(a, b) {
                    a.innerHTML = "";
                    a.style.width = n[b]
                }, j);
                if (h(a.nTable).outerWidth() < i) {
                    g = d.scrollHeight > d.offsetHeight || "scroll" == h(d).css("overflow-y") ? i + a.oScroll.iBarWidth : i;
                    if (r && (d.scrollHeight > d.offsetHeight || "scroll" == h(d).css("overflow-y"))) a.nTable.style.width = q(g - a.oScroll.iBarWidth);
                    d.style.width = q(g);
                    a.nScrollHead.style.width = q(g);
                    null !== a.nTFoot && (a.nScrollFoot.style.width = q(g));
                    "" === a.oScroll.sX ? D(a, 1, "The table cannot fit into the current element which will cause column misalignment. The table has been drawn at its minimum possible width.") :
                        "" !== a.oScroll.sXInner && D(a, 1, "The table cannot fit into the current element which will cause column misalignment. Increase the sScrollXInner value or remove it to allow automatic calculation")
                } else d.style.width = q("100%"), a.nScrollHead.style.width = q("100%"), null !== a.nTFoot && (a.nScrollFoot.style.width = q("100%"));
                "" === a.oScroll.sY && r && (d.style.height = q(a.nTable.offsetHeight + a.oScroll.iBarWidth));
                "" !== a.oScroll.sY && a.oScroll.bCollapse && (d.style.height = q(a.oScroll.sY), r = "" !== a.oScroll.sX && a.nTable.offsetWidth >
                    d.offsetWidth ? a.oScroll.iBarWidth : 0, a.nTable.offsetHeight < d.offsetHeight && (d.style.height = q(a.nTable.offsetHeight + r)));
                r = h(a.nTable).outerWidth();
                c.style.width = q(r);
                b.style.width = q(r);
                c = h(a.nTable).height() > d.clientHeight || "scroll" == h(d).css("overflow-y");
                b.style.paddingRight = c ? a.oScroll.iBarWidth + "px" : "0px";
                null !== a.nTFoot && (R.style.width = q(r), l.style.width = q(r), l.style.paddingRight = c ? a.oScroll.iBarWidth + "px" : "0px");
                h(d).scroll();
                if (a.bSorted || a.bFiltered) d.scrollTop = 0
            }

            function C(a, b, c) {
                for (var d =
                        0, i = 0, f = b.length, g, e; i < f;) {
                    g = b[i].firstChild;
                    for (e = c ? c[i].firstChild : null; g;) 1 === g.nodeType && (c ? a(g, e, d) : a(g, d), d++), g = g.nextSibling, e = c ? e.nextSibling : null;
                    i++
                }
            }

            function Ma(a, b) {
                if (!a || null === a || "" === a) return 0;
                b || (b = l.body);
                var c, d = l.createElement("div");
                d.style.width = q(a);
                b.appendChild(d);
                c = d.offsetWidth;
                b.removeChild(d);
                return c
            }

            function da(a) {
                var b = 0,
                    c, d = 0,
                    i = a.aoColumns.length,
                    f, e, j = h("th", a.nTHead),
                    o = a.nTable.getAttribute("width");
                e = a.nTable.parentNode;
                for (f = 0; f < i; f++) a.aoColumns[f].bVisible &&
                    (d++, null !== a.aoColumns[f].sWidth && (c = Ma(a.aoColumns[f].sWidthOrig, e), null !== c && (a.aoColumns[f].sWidth = q(c)), b++));
                if (i == j.length && 0 === b && d == i && "" === a.oScroll.sX && "" === a.oScroll.sY)
                    for (f = 0; f < a.aoColumns.length; f++) c = h(j[f]).width(), null !== c && (a.aoColumns[f].sWidth = q(c));
                else {
                    b = a.nTable.cloneNode(!1);
                    f = a.nTHead.cloneNode(!0);
                    d = l.createElement("tbody");
                    c = l.createElement("tr");
                    b.removeAttribute("id");
                    b.appendChild(f);
                    null !== a.nTFoot && (b.appendChild(a.nTFoot.cloneNode(!0)), C(function(a) {
                        a.style.width =
                            ""
                    }, b.getElementsByTagName("tr")));
                    b.appendChild(d);
                    d.appendChild(c);
                    d = h("thead th", b);
                    0 === d.length && (d = h("tbody tr:eq(0)>td", b));
                    j = N(a, f);
                    for (f = d = 0; f < i; f++) {
                        var k = a.aoColumns[f];
                        k.bVisible && null !== k.sWidthOrig && "" !== k.sWidthOrig ? j[f - d].style.width = q(k.sWidthOrig) : k.bVisible ? j[f - d].style.width = "" : d++
                    }
                    for (f = 0; f < i; f++) a.aoColumns[f].bVisible && (d = Na(a, f), null !== d && (d = d.cloneNode(!0), "" !== a.aoColumns[f].sContentPadding && (d.innerHTML += a.aoColumns[f].sContentPadding), c.appendChild(d)));
                    e.appendChild(b);
                    "" !== a.oScroll.sX && "" !== a.oScroll.sXInner ? b.style.width = q(a.oScroll.sXInner) : "" !== a.oScroll.sX ? (b.style.width = "", h(b).width() < e.offsetWidth && (b.style.width = q(e.offsetWidth))) : "" !== a.oScroll.sY ? b.style.width = q(e.offsetWidth) : o && (b.style.width = q(o));
                    b.style.visibility = "hidden";
                    Oa(a, b);
                    i = h("tbody tr:eq(0)", b).children();
                    0 === i.length && (i = N(a, h("thead", b)[0]));
                    if ("" !== a.oScroll.sX) {
                        for (f = d = e = 0; f < a.aoColumns.length; f++) a.aoColumns[f].bVisible && (e = null === a.aoColumns[f].sWidthOrig ? e + h(i[d]).outerWidth() :
                            e + (parseInt(a.aoColumns[f].sWidth.replace("px", ""), 10) + (h(i[d]).outerWidth() - h(i[d]).width())), d++);
                        b.style.width = q(e);
                        a.nTable.style.width = q(e)
                    }
                    for (f = d = 0; f < a.aoColumns.length; f++) a.aoColumns[f].bVisible && (e = h(i[d]).width(), null !== e && 0 < e && (a.aoColumns[f].sWidth = q(e)), d++);
                    i = h(b).css("width");
                    a.nTable.style.width = -1 !== i.indexOf("%") ? i : q(h(b).outerWidth());
                    b.parentNode.removeChild(b)
                }
                o && (a.nTable.style.width = q(o))
            }

            function Oa(a, b) {
                "" === a.oScroll.sX && "" !== a.oScroll.sY ? (h(b).width(), b.style.width = q(h(b).outerWidth() -
                    a.oScroll.iBarWidth)) : "" !== a.oScroll.sX && (b.style.width = q(h(b).outerWidth()))
            }

            function Na(a, b) {
                var c = Pa(a, b);
                if (0 > c) return null;
                if (null === a.aoData[c].nTr) {
                    var d = l.createElement("td");
                    d.innerHTML = v(a, c, b, "");
                    return d
                }
                return J(a, c)[b]
            }

            function Pa(a, b) {
                for (var c = -1, d = -1, i = 0; i < a.aoData.length; i++) {
                    var e = v(a, i, b, "display") + "",
                        e = e.replace(/<.*?>/g, "");
                    e.length > c && (c = e.length, d = i)
                }
                return d
            }

            function q(a) {
                if (null === a) return "0px";
                if ("number" == typeof a) return 0 > a ? "0px" : a + "px";
                var b = a.charCodeAt(a.length - 1);
                return 48 > b || 57 < b ? a : a + "px"
            }

            function Qa() {
                var a = l.createElement("p"),
                    b = a.style;
                b.width = "100%";
                b.height = "200px";
                b.padding = "0px";
                var c = l.createElement("div"),
                    b = c.style;
                b.position = "absolute";
                b.top = "0px";
                b.left = "0px";
                b.visibility = "hidden";
                b.width = "200px";
                b.height = "150px";
                b.padding = "0px";
                b.overflow = "hidden";
                c.appendChild(a);
                l.body.appendChild(c);
                b = a.offsetWidth;
                c.style.overflow = "scroll";
                a = a.offsetWidth;
                b == a && (a = c.clientWidth);
                l.body.removeChild(c);
                return b - a
            }

            function O(a, b) {
                var c, d, i, e, g, k, o = [],
                    m = [],
                    p =
                    j.ext.oSort,
                    l = a.aoData,
                    q = a.aoColumns,
                    G = a.oLanguage.oAria;
                if (!a.oFeatures.bServerSide && (0 !== a.aaSorting.length || null !== a.aaSortingFixed)) {
                    o = null !== a.aaSortingFixed ? a.aaSortingFixed.concat(a.aaSorting) : a.aaSorting.slice();
                    for (c = 0; c < o.length; c++)
                        if (d = o[c][0], i = R(a, d), e = a.aoColumns[d].sSortDataType, j.ext.afnSortData[e])
                            if (g = j.ext.afnSortData[e].call(a.oInstance, a, d, i), g.length === l.length) {
                                i = 0;
                                for (e = l.length; i < e; i++) F(a, i, d, g[i])
                            } else D(a, 0, "Returned data sort array (col " + d + ") is the wrong length");
                    c =
                        0;
                    for (d = a.aiDisplayMaster.length; c < d; c++) m[a.aiDisplayMaster[c]] = c;
                    var r = o.length,
                        s;
                    c = 0;
                    for (d = l.length; c < d; c++)
                        for (i = 0; i < r; i++) {
                            s = q[o[i][0]].aDataSort;
                            g = 0;
                            for (k = s.length; g < k; g++) e = q[s[g]].sType, e = p[(e ? e : "string") + "-pre"], l[c]._aSortData[s[g]] = e ? e(v(a, c, s[g], "sort")) : v(a, c, s[g], "sort")
                        }
                    a.aiDisplayMaster.sort(function(a, b) {
                        var c, d, e, i, f;
                        for (c = 0; c < r; c++) {
                            f = q[o[c][0]].aDataSort;
                            d = 0;
                            for (e = f.length; d < e; d++)
                                if (i = q[f[d]].sType, i = p[(i ? i : "string") + "-" + o[c][1]](l[a]._aSortData[f[d]], l[b]._aSortData[f[d]]), 0 !==
                                    i) return i
                        }
                        return p["numeric-asc"](m[a], m[b])
                    })
                }(b === n || b) && !a.oFeatures.bDeferRender && P(a);
                c = 0;
                for (d = a.aoColumns.length; c < d; c++) e = q[c].sTitle.replace(/<.*?>/g, ""), i = q[c].nTh, i.removeAttribute("aria-sort"), i.removeAttribute("aria-label"), q[c].bSortable ? 0 < o.length && o[0][0] == c ? (i.setAttribute("aria-sort", "asc" == o[0][1] ? "ascending" : "descending"), i.setAttribute("aria-label", e + ("asc" == (q[c].asSorting[o[0][2] + 1] ? q[c].asSorting[o[0][2] + 1] : q[c].asSorting[0]) ? G.sSortAscending : G.sSortDescending))) : i.setAttribute("aria-label",
                    e + ("asc" == q[c].asSorting[0] ? G.sSortAscending : G.sSortDescending)) : i.setAttribute("aria-label", e);
                a.bSorted = !0;
                h(a.oInstance).trigger("sort", a);
                a.oFeatures.bFilter ? K(a, a.oPreviousSearch, 1) : (a.aiDisplay = a.aiDisplayMaster.slice(), a._iDisplayStart = 0, y(a), x(a))
            }

            function ia(a, b, c, d) {
                Ra(b, {}, function(b) {
                    if (!1 !== a.aoColumns[c].bSortable) {
                        var e = function() {
                            var d, e;
                            if (b.shiftKey) {
                                for (var f = !1, h = 0; h < a.aaSorting.length; h++)
                                    if (a.aaSorting[h][0] == c) {
                                        f = !0;
                                        d = a.aaSorting[h][0];
                                        e = a.aaSorting[h][2] + 1;
                                        a.aoColumns[d].asSorting[e] ?
                                            (a.aaSorting[h][1] = a.aoColumns[d].asSorting[e], a.aaSorting[h][2] = e) : a.aaSorting.splice(h, 1);
                                        break
                                    }!1 === f && a.aaSorting.push([c, a.aoColumns[c].asSorting[0], 0])
                            } else 1 == a.aaSorting.length && a.aaSorting[0][0] == c ? (d = a.aaSorting[0][0], e = a.aaSorting[0][2] + 1, a.aoColumns[d].asSorting[e] || (e = 0), a.aaSorting[0][1] = a.aoColumns[d].asSorting[e], a.aaSorting[0][2] = e) : (a.aaSorting.splice(0, a.aaSorting.length), a.aaSorting.push([c, a.aoColumns[c].asSorting[0], 0]));
                            O(a)
                        };
                        a.oFeatures.bProcessing ? (E(a, !0), setTimeout(function() {
                            e();
                            a.oFeatures.bServerSide || E(a, !1)
                        }, 0)) : e();
                        "function" == typeof d && d(a)
                    }
                })
            }

            function P(a) {
                var b, c, d, e, f, g = a.aoColumns.length,
                    j = a.oClasses;
                for (b = 0; b < g; b++) a.aoColumns[b].bSortable && h(a.aoColumns[b].nTh).removeClass(j.sSortAsc + " " + j.sSortDesc + " " + a.aoColumns[b].sSortingClass);
                c = null !== a.aaSortingFixed ? a.aaSortingFixed.concat(a.aaSorting) : a.aaSorting.slice();
                for (b = 0; b < a.aoColumns.length; b++)
                    if (a.aoColumns[b].bSortable) {
                        f = a.aoColumns[b].sSortingClass;
                        e = -1;
                        for (d = 0; d < c.length; d++)
                            if (c[d][0] == b) {
                                f = "asc" == c[d][1] ?
                                    j.sSortAsc : j.sSortDesc;
                                e = d;
                                break
                            }
                        h(a.aoColumns[b].nTh).addClass(f);
                        a.bJUI && (f = h("span." + j.sSortIcon, a.aoColumns[b].nTh), f.removeClass(j.sSortJUIAsc + " " + j.sSortJUIDesc + " " + j.sSortJUI + " " + j.sSortJUIAscAllowed + " " + j.sSortJUIDescAllowed), f.addClass(-1 == e ? a.aoColumns[b].sSortingClassJUI : "asc" == c[e][1] ? j.sSortJUIAsc : j.sSortJUIDesc))
                    } else h(a.aoColumns[b].nTh).addClass(a.aoColumns[b].sSortingClass);
                f = j.sSortColumn;
                if (a.oFeatures.bSort && a.oFeatures.bSortClasses) {
                    a = J(a);
                    e = [];
                    for (b = 0; b < g; b++) e.push("");
                    b = 0;
                    for (d = 1; b < c.length; b++) j = parseInt(c[b][0], 10), e[j] = f + d, 3 > d && d++;
                    f = RegExp(f + "[123]");
                    var o;
                    b = 0;
                    for (c = a.length; b < c; b++) j = b % g, d = a[b].className, o = e[j], j = d.replace(f, o), j != d ? a[b].className = h.trim(j) : 0 < o.length && -1 == d.indexOf(o) && (a[b].className = d + " " + o)
                }
            }

            function ra(a) {
                if (a.oFeatures.bStateSave && !a.bDestroying) {
                    var b, c;
                    b = a.oScroll.bInfinite;
                    var d = {
                        iCreate: (new Date).getTime(),
                        iStart: b ? 0 : a._iDisplayStart,
                        iEnd: b ? a._iDisplayLength : a._iDisplayEnd,
                        iLength: a._iDisplayLength,
                        aaSorting: h.extend(!0, [], a.aaSorting),
                        oSearch: h.extend(!0, {}, a.oPreviousSearch),
                        aoSearchCols: h.extend(!0, [], a.aoPreSearchCols),
                        abVisCols: []
                    };
                    b = 0;
                    for (c = a.aoColumns.length; b < c; b++) d.abVisCols.push(a.aoColumns[b].bVisible);
                    A(a, "aoStateSaveParams", "stateSaveParams", [a, d]);
                    a.fnStateSave.call(a.oInstance, a, d)
                }
            }

            function Sa(a, b) {
                if (a.oFeatures.bStateSave) {
                    var c = a.fnStateLoad.call(a.oInstance, a);
                    if (c) {
                        var d = A(a, "aoStateLoadParams", "stateLoadParams", [a, c]);
                        if (-1 === h.inArray(!1, d)) {
                            a.oLoadedState = h.extend(!0, {}, c);
                            a._iDisplayStart = c.iStart;
                            a.iInitDisplayStart =
                                c.iStart;
                            a._iDisplayEnd = c.iEnd;
                            a._iDisplayLength = c.iLength;
                            a.aaSorting = c.aaSorting.slice();
                            a.saved_aaSorting = c.aaSorting.slice();
                            h.extend(a.oPreviousSearch, c.oSearch);
                            h.extend(!0, a.aoPreSearchCols, c.aoSearchCols);
                            b.saved_aoColumns = [];
                            for (d = 0; d < c.abVisCols.length; d++) b.saved_aoColumns[d] = {}, b.saved_aoColumns[d].bVisible = c.abVisCols[d];
                            A(a, "aoStateLoaded", "stateLoaded", [a, c])
                        }
                    }
                }
            }

            function s(a) {
                for (var b = 0; b < j.settings.length; b++)
                    if (j.settings[b].nTable === a) return j.settings[b];
                return null
            }

            function T(a) {
                for (var b = [], a = a.aoData, c = 0, d = a.length; c < d; c++) null !== a[c].nTr && b.push(a[c].nTr);
                return b
            }

            function J(a, b) {
                var c = [],
                    d, e, f, g, h, j;
                e = 0;
                var o = a.aoData.length;
                b !== n && (e = b, o = b + 1);
                for (f = e; f < o; f++)
                    if (j = a.aoData[f], null !== j.nTr) {
                        e = [];
                        for (d = j.nTr.firstChild; d;) g = d.nodeName.toLowerCase(), ("td" == g || "th" == g) && e.push(d), d = d.nextSibling;
                        g = d = 0;
                        for (h = a.aoColumns.length; g < h; g++) a.aoColumns[g].bVisible ? c.push(e[g - d]) : (c.push(j._anHidden[g]), d++)
                    }
                return c
            }

            function D(a, b, c) {
                a = null === a ? "DataTables warning: " + c : "DataTables warning (table id = '" +
                    a.sTableId + "'): " + c;
                if (0 === b)
                    if ("alert" == j.ext.sErrMode) alert(a);
                    else throw Error(a);
                else X.console && console.log && console.log(a)
            }

            function p(a, b, c, d) {
                d === n && (d = c);
                b[c] !== n && (a[d] = b[c])
            }

            function Ta(a, b) {
                var c, d;
                for (d in b) b.hasOwnProperty(d) && (c = b[d], "object" === typeof e[d] && null !== c && !1 === h.isArray(c) ? h.extend(!0, a[d], c) : a[d] = c);
                return a
            }

            function Ra(a, b, c) {
                h(a).bind("click.DT", b, function(b) {
                    a.blur();
                    c(b)
                }).bind("keypress.DT", b, function(a) {
                    13 === a.which && c(a)
                }).bind("selectstart.DT", function() {
                    return !1
                })
            }

            function z(a, b, c, d) {
                c && a[b].push({
                    fn: c,
                    sName: d
                })
            }

            function A(a, b, c, d) {
                for (var b = a[b], e = [], f = b.length - 1; 0 <= f; f--) e.push(b[f].fn.apply(a.oInstance, d));
                null !== c && h(a.oInstance).trigger(c, d);
                return e
            }

            function Ua(a) {
                var b = h('<div style="position:absolute; top:0; left:0; height:1px; width:1px; overflow:hidden"><div style="position:absolute; top:1px; left:1px; width:100px; overflow:scroll;"><div id="DT_BrowserTest" style="width:100%; height:10px;"></div></div></div>')[0];
                l.body.appendChild(b);
                a.oBrowser.bScrollOversize =
                    100 === h("#DT_BrowserTest", b)[0].offsetWidth ? !0 : !1;
                l.body.removeChild(b)
            }

            function Va(a) {
                return function() {
                    var b = [s(this[j.ext.iApiIndex])].concat(Array.prototype.slice.call(arguments));
                    return j.ext.oApi[a].apply(this, b)
                }
            }
            var U = /\[.*?\]$/,
                Wa = X.JSON ? JSON.stringify : function(a) {
                    var b = typeof a;
                    if ("object" !== b || null === a) return "string" === b && (a = '"' + a + '"'), a + "";
                    var c, d, e = [],
                        f = h.isArray(a);
                    for (c in a) d = a[c], b = typeof d, "string" === b ? d = '"' + d + '"' : "object" === b && null !== d && (d = Wa(d)), e.push((f ? "" : '"' + c + '":') + d);
                    return (f ?
                        "[" : "{") + e + (f ? "]" : "}")
                };
            this.$ = function(a, b) {
                var c, d, e = [],
                    f;
                d = s(this[j.ext.iApiIndex]);
                var g = d.aoData,
                    o = d.aiDisplay,
                    k = d.aiDisplayMaster;
                b || (b = {});
                b = h.extend({}, {
                    filter: "none",
                    order: "current",
                    page: "all"
                }, b);
                if ("current" == b.page) {
                    c = d._iDisplayStart;
                    for (d = d.fnDisplayEnd(); c < d; c++)(f = g[o[c]].nTr) && e.push(f)
                } else if ("current" == b.order && "none" == b.filter) {
                    c = 0;
                    for (d = k.length; c < d; c++)(f = g[k[c]].nTr) && e.push(f)
                } else if ("current" == b.order && "applied" == b.filter) {
                    c = 0;
                    for (d = o.length; c < d; c++)(f = g[o[c]].nTr) && e.push(f)
                } else if ("original" ==
                    b.order && "none" == b.filter) {
                    c = 0;
                    for (d = g.length; c < d; c++)(f = g[c].nTr) && e.push(f)
                } else if ("original" == b.order && "applied" == b.filter) {
                    c = 0;
                    for (d = g.length; c < d; c++) f = g[c].nTr, -1 !== h.inArray(c, o) && f && e.push(f)
                } else D(d, 1, "Unknown selection options");
                e = h(e);
                c = e.filter(a);
                e = e.find(a);
                return h([].concat(h.makeArray(c), h.makeArray(e)))
            };
            this._ = function(a, b) {
                var c = [],
                    d, e, f = this.$(a, b);
                d = 0;
                for (e = f.length; d < e; d++) c.push(this.fnGetData(f[d]));
                return c
            };
            this.fnAddData = function(a, b) {
                if (0 === a.length) return [];
                var c = [],
                    d, e = s(this[j.ext.iApiIndex]);
                if ("object" === typeof a[0] && null !== a[0])
                    for (var f = 0; f < a.length; f++) {
                        d = H(e, a[f]);
                        if (-1 == d) return c;
                        c.push(d)
                    } else {
                        d = H(e, a);
                        if (-1 == d) return c;
                        c.push(d)
                    }
                e.aiDisplay = e.aiDisplayMaster.slice();
                (b === n || b) && aa(e);
                return c
            };
            this.fnAdjustColumnSizing = function(a) {
                var b = s(this[j.ext.iApiIndex]);
                k(b);
                a === n || a ? this.fnDraw(!1) : ("" !== b.oScroll.sX || "" !== b.oScroll.sY) && this.oApi._fnScrollDraw(b)
            };
            this.fnClearTable = function(a) {
                var b = s(this[j.ext.iApiIndex]);
                ga(b);
                (a === n || a) && x(b)
            };
            this.fnClose =
                function(a) {
                    for (var b = s(this[j.ext.iApiIndex]), c = 0; c < b.aoOpenRows.length; c++)
                        if (b.aoOpenRows[c].nParent == a) return (a = b.aoOpenRows[c].nTr.parentNode) && a.removeChild(b.aoOpenRows[c].nTr), b.aoOpenRows.splice(c, 1), 0;
                    return 1
                };
            this.fnDeleteRow = function(a, b, c) {
                var d = s(this[j.ext.iApiIndex]),
                    e, f, a = "object" === typeof a ? I(d, a) : a,
                    g = d.aoData.splice(a, 1);
                e = 0;
                for (f = d.aoData.length; e < f; e++) null !== d.aoData[e].nTr && (d.aoData[e].nTr._DT_RowIndex = e);
                e = h.inArray(a, d.aiDisplay);
                d.asDataSearch.splice(e, 1);
                ha(d.aiDisplayMaster,
                    a);
                ha(d.aiDisplay, a);
                "function" === typeof b && b.call(this, d, g);
                d._iDisplayStart >= d.fnRecordsDisplay() && (d._iDisplayStart -= d._iDisplayLength, 0 > d._iDisplayStart && (d._iDisplayStart = 0));
                if (c === n || c) y(d), x(d);
                return g
            };
            this.fnDestroy = function(a) {
                var b = s(this[j.ext.iApiIndex]),
                    c = b.nTableWrapper.parentNode,
                    d = b.nTBody,
                    i, f, a = a === n ? !1 : a;
                b.bDestroying = !0;
                A(b, "aoDestroyCallback", "destroy", [b]);
                if (!a) {
                    i = 0;
                    for (f = b.aoColumns.length; i < f; i++) !1 === b.aoColumns[i].bVisible && this.fnSetColumnVis(i, !0)
                }
                h(b.nTableWrapper).find("*").andSelf().unbind(".DT");
                h("tbody>tr>td." + b.oClasses.sRowEmpty, b.nTable).parent().remove();
                b.nTable != b.nTHead.parentNode && (h(b.nTable).children("thead").remove(), b.nTable.appendChild(b.nTHead));
                b.nTFoot && b.nTable != b.nTFoot.parentNode && (h(b.nTable).children("tfoot").remove(), b.nTable.appendChild(b.nTFoot));
                b.nTable.parentNode.removeChild(b.nTable);
                h(b.nTableWrapper).remove();
                b.aaSorting = [];
                b.aaSortingFixed = [];
                P(b);
                h(T(b)).removeClass(b.asStripeClasses.join(" "));
                h("th, td", b.nTHead).removeClass([b.oClasses.sSortable, b.oClasses.sSortableAsc,
                    b.oClasses.sSortableDesc, b.oClasses.sSortableNone
                ].join(" "));
                b.bJUI && (h("th span." + b.oClasses.sSortIcon + ", td span." + b.oClasses.sSortIcon, b.nTHead).remove(), h("th, td", b.nTHead).each(function() {
                    var a = h("div." + b.oClasses.sSortJUIWrapper, this),
                        c = a.contents();
                    h(this).append(c);
                    a.remove()
                }));
                !a && b.nTableReinsertBefore ? c.insertBefore(b.nTable, b.nTableReinsertBefore) : a || c.appendChild(b.nTable);
                i = 0;
                for (f = b.aoData.length; i < f; i++) null !== b.aoData[i].nTr && d.appendChild(b.aoData[i].nTr);
                !0 === b.oFeatures.bAutoWidth &&
                    (b.nTable.style.width = q(b.sDestroyWidth));
                if (f = b.asDestroyStripes.length) {
                    a = h(d).children("tr");
                    for (i = 0; i < f; i++) a.filter(":nth-child(" + f + "n + " + i + ")").addClass(b.asDestroyStripes[i])
                }
                i = 0;
                for (f = j.settings.length; i < f; i++) j.settings[i] == b && j.settings.splice(i, 1);
                e = b = null
            };
            this.fnDraw = function(a) {
                var b = s(this[j.ext.iApiIndex]);
                !1 === a ? (y(b), x(b)) : aa(b)
            };
            this.fnFilter = function(a, b, c, d, e, f) {
                var g = s(this[j.ext.iApiIndex]);
                if (g.oFeatures.bFilter) {
                    if (c === n || null === c) c = !1;
                    if (d === n || null === d) d = !0;
                    if (e === n || null ===
                        e) e = !0;
                    if (f === n || null === f) f = !0;
                    if (b === n || null === b) {
                        if (K(g, {
                                sSearch: a + "",
                                bRegex: c,
                                bSmart: d,
                                bCaseInsensitive: f
                            }, 1), e && g.aanFeatures.f) {
                            b = g.aanFeatures.f;
                            c = 0;
                            for (d = b.length; c < d; c++) try {
                                b[c]._DT_Input != l.activeElement && h(b[c]._DT_Input).val(a)
                            } catch (o) {
                                h(b[c]._DT_Input).val(a)
                            }
                        }
                    } else h.extend(g.aoPreSearchCols[b], {
                        sSearch: a + "",
                        bRegex: c,
                        bSmart: d,
                        bCaseInsensitive: f
                    }), K(g, g.oPreviousSearch, 1)
                }
            };
            this.fnGetData = function(a, b) {
                var c = s(this[j.ext.iApiIndex]);
                if (a !== n) {
                    var d = a;
                    if ("object" === typeof a) {
                        var e = a.nodeName.toLowerCase();
                        "tr" === e ? d = I(c, a) : "td" === e && (d = I(c, a.parentNode), b = fa(c, d, a))
                    }
                    return b !== n ? v(c, d, b, "") : c.aoData[d] !== n ? c.aoData[d]._aData : null
                }
                return Z(c)
            };
            this.fnGetNodes = function(a) {
                var b = s(this[j.ext.iApiIndex]);
                return a !== n ? b.aoData[a] !== n ? b.aoData[a].nTr : null : T(b)
            };
            this.fnGetPosition = function(a) {
                var b = s(this[j.ext.iApiIndex]),
                    c = a.nodeName.toUpperCase();
                return "TR" == c ? I(b, a) : "TD" == c || "TH" == c ? (c = I(b, a.parentNode), a = fa(b, c, a), [c, R(b, a), a]) : null
            };
            this.fnIsOpen = function(a) {
                for (var b = s(this[j.ext.iApiIndex]), c = 0; c <
                    b.aoOpenRows.length; c++)
                    if (b.aoOpenRows[c].nParent == a) return !0;
                return !1
            };
            this.fnOpen = function(a, b, c) {
                var d = s(this[j.ext.iApiIndex]),
                    e = T(d);
                if (-1 !== h.inArray(a, e)) {
                    this.fnClose(a);
                    var e = l.createElement("tr"),
                        f = l.createElement("td");
                    e.appendChild(f);
                    f.className = c;
                    f.colSpan = t(d);
                    "string" === typeof b ? f.innerHTML = b : h(f).html(b);
                    b = h("tr", d.nTBody); - 1 != h.inArray(a, b) && h(e).insertAfter(a);
                    d.aoOpenRows.push({
                        nTr: e,
                        nParent: a
                    });
                    return e
                }
            };
            this.fnPageChange = function(a, b) {
                var c = s(this[j.ext.iApiIndex]);
                qa(c, a);
                y(c);
                (b === n || b) && x(c)
            };
            this.fnSetColumnVis = function(a, b, c) {
                var d = s(this[j.ext.iApiIndex]),
                    e, f, g = d.aoColumns,
                    h = d.aoData,
                    o, m;
                if (g[a].bVisible != b) {
                    if (b) {
                        for (e = f = 0; e < a; e++) g[e].bVisible && f++;
                        m = f >= t(d);
                        if (!m)
                            for (e = a; e < g.length; e++)
                                if (g[e].bVisible) {
                                    o = e;
                                    break
                                }
                        e = 0;
                        for (f = h.length; e < f; e++) null !== h[e].nTr && (m ? h[e].nTr.appendChild(h[e]._anHidden[a]) : h[e].nTr.insertBefore(h[e]._anHidden[a], J(d, e)[o]))
                    } else {
                        e = 0;
                        for (f = h.length; e < f; e++) null !== h[e].nTr && (o = J(d, e)[a], h[e]._anHidden[a] = o, o.parentNode.removeChild(o))
                    }
                    g[a].bVisible =
                        b;
                    W(d, d.aoHeader);
                    d.nTFoot && W(d, d.aoFooter);
                    e = 0;
                    for (f = d.aoOpenRows.length; e < f; e++) d.aoOpenRows[e].nTr.colSpan = t(d);
                    if (c === n || c) k(d), x(d);
                    ra(d)
                }
            };
            this.fnSettings = function() {
                return s(this[j.ext.iApiIndex])
            };
            this.fnSort = function(a) {
                var b = s(this[j.ext.iApiIndex]);
                b.aaSorting = a;
                O(b)
            };
            this.fnSortListener = function(a, b, c) {
                ia(s(this[j.ext.iApiIndex]), a, b, c)
            };
            this.fnUpdate = function(a, b, c, d, e) {
                var f = s(this[j.ext.iApiIndex]),
                    b = "object" === typeof b ? I(f, b) : b;
                if (h.isArray(a) && c === n) {
                    f.aoData[b]._aData = a.slice();
                    for (c = 0; c < f.aoColumns.length; c++) this.fnUpdate(v(f, b, c), b, c, !1, !1)
                } else if (h.isPlainObject(a) && c === n) {
                    f.aoData[b]._aData = h.extend(!0, {}, a);
                    for (c = 0; c < f.aoColumns.length; c++) this.fnUpdate(v(f, b, c), b, c, !1, !1)
                } else {
                    F(f, b, c, a);
                    var a = v(f, b, c, "display"),
                        g = f.aoColumns[c];
                    null !== g.fnRender && (a = S(f, b, c), g.bUseRendered && F(f, b, c, a));
                    null !== f.aoData[b].nTr && (J(f, b)[c].innerHTML = a)
                }
                c = h.inArray(b, f.aiDisplay);
                f.asDataSearch[c] = na(f, Y(f, b, "filter", r(f, "bSearchable")));
                (e === n || e) && k(f);
                (d === n || d) && aa(f);
                return 0
            };
            this.fnVersionCheck = j.ext.fnVersionCheck;
            this.oApi = {
                _fnExternApiFunc: Va,
                _fnInitialise: ba,
                _fnInitComplete: $,
                _fnLanguageCompat: pa,
                _fnAddColumn: o,
                _fnColumnOptions: m,
                _fnAddData: H,
                _fnCreateTr: ea,
                _fnGatherData: ua,
                _fnBuildHead: va,
                _fnDrawHead: W,
                _fnDraw: x,
                _fnReDraw: aa,
                _fnAjaxUpdate: wa,
                _fnAjaxParameters: Ea,
                _fnAjaxUpdateDraw: Fa,
                _fnServerParams: ka,
                _fnAddOptionsHtml: xa,
                _fnFeatureHtmlTable: Ba,
                _fnScrollDraw: La,
                _fnAdjustColumnSizing: k,
                _fnFeatureHtmlFilter: za,
                _fnFilterComplete: K,
                _fnFilterCustom: Ia,
                _fnFilterColumn: Ha,
                _fnFilter: Ga,
                _fnBuildSearchArray: la,
                _fnBuildSearchRow: na,
                _fnFilterCreateSearch: ma,
                _fnDataToSearch: Ja,
                _fnSort: O,
                _fnSortAttachListener: ia,
                _fnSortingClasses: P,
                _fnFeatureHtmlPaginate: Da,
                _fnPageChange: qa,
                _fnFeatureHtmlInfo: Ca,
                _fnUpdateInfo: Ka,
                _fnFeatureHtmlLength: ya,
                _fnFeatureHtmlProcessing: Aa,
                _fnProcessingDisplay: E,
                _fnVisibleToColumnIndex: G,
                _fnColumnIndexToVisible: R,
                _fnNodeToDataIndex: I,
                _fnVisbleColumns: t,
                _fnCalculateEnd: y,
                _fnConvertToWidth: Ma,
                _fnCalculateColumnWidths: da,
                _fnScrollingWidthAdjust: Oa,
                _fnGetWidestNode: Na,
                _fnGetMaxLenString: Pa,
                _fnStringToCss: q,
                _fnDetectType: B,
                _fnSettingsFromNode: s,
                _fnGetDataMaster: Z,
                _fnGetTrNodes: T,
                _fnGetTdNodes: J,
                _fnEscapeRegex: oa,
                _fnDeleteIndex: ha,
                _fnReOrderIndex: u,
                _fnColumnOrdering: M,
                _fnLog: D,
                _fnClearTable: ga,
                _fnSaveState: ra,
                _fnLoadState: Sa,
                _fnCreateCookie: function(a, b, c, d, e) {
                    var f = new Date;
                    f.setTime(f.getTime() + 1E3 * c);
                    var c = X.location.pathname.split("/"),
                        a = a + "_" + c.pop().replace(/[\/:]/g, "").toLowerCase(),
                        g;
                    null !== e ? (g = "function" === typeof h.parseJSON ? h.parseJSON(b) : eval("(" + b + ")"),
                        b = e(a, g, f.toGMTString(), c.join("/") + "/")) : b = a + "=" + encodeURIComponent(b) + "; expires=" + f.toGMTString() + "; path=" + c.join("/") + "/";
                    a = l.cookie.split(";");
                    e = b.split(";")[0].length;
                    f = [];
                    if (4096 < e + l.cookie.length + 10) {
                        for (var j = 0, o = a.length; j < o; j++)
                            if (-1 != a[j].indexOf(d)) {
                                var k = a[j].split("=");
                                try {
                                    (g = eval("(" + decodeURIComponent(k[1]) + ")")) && g.iCreate && f.push({
                                        name: k[0],
                                        time: g.iCreate
                                    })
                                } catch (m) {}
                            }
                        for (f.sort(function(a, b) {
                                return b.time - a.time
                            }); 4096 < e + l.cookie.length + 10;) {
                            if (0 === f.length) return;
                            d = f.pop();
                            l.cookie =
                                d.name + "=; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=" + c.join("/") + "/"
                        }
                    }
                    l.cookie = b
                },
                _fnReadCookie: function(a) {
                    for (var b = X.location.pathname.split("/"), a = a + "_" + b[b.length - 1].replace(/[\/:]/g, "").toLowerCase() + "=", b = l.cookie.split(";"), c = 0; c < b.length; c++) {
                        for (var d = b[c];
                            " " == d.charAt(0);) d = d.substring(1, d.length);
                        if (0 === d.indexOf(a)) return decodeURIComponent(d.substring(a.length, d.length))
                    }
                    return null
                },
                _fnDetectHeader: V,
                _fnGetUniqueThs: N,
                _fnScrollBarWidth: Qa,
                _fnApplyToChildren: C,
                _fnMap: p,
                _fnGetRowData: Y,
                _fnGetCellData: v,
                _fnSetCellData: F,
                _fnGetObjectDataFn: Q,
                _fnSetObjectDataFn: L,
                _fnApplyColumnDefs: ta,
                _fnBindAction: Ra,
                _fnExtend: Ta,
                _fnCallbackReg: z,
                _fnCallbackFire: A,
                _fnJsonString: Wa,
                _fnRender: S,
                _fnNodeToColumnIndex: fa,
                _fnInfoMacros: ja,
                _fnBrowserDetect: Ua,
                _fnGetColumns: r
            };
            h.extend(j.ext.oApi, this.oApi);
            for (var sa in j.ext.oApi) sa && (this[sa] = Va(sa));
            var ca = this;
            this.each(function() {
                var a = 0,
                    b, c, d;
                c = this.getAttribute("id");
                var i = !1,
                    f = !1;
                if ("table" != this.nodeName.toLowerCase()) D(null, 0, "Attempted to initialise DataTables on a node which is not a table: " +
                    this.nodeName);
                else {
                    a = 0;
                    for (b = j.settings.length; a < b; a++) {
                        if (j.settings[a].nTable == this) {
                            if (e === n || e.bRetrieve) return j.settings[a].oInstance;
                            if (e.bDestroy) {
                                j.settings[a].oInstance.fnDestroy();
                                break
                            } else {
                                D(j.settings[a], 0, "Cannot reinitialise DataTable.\n\nTo retrieve the DataTables object for this table, pass no arguments or see the docs for bRetrieve and bDestroy");
                                return
                            }
                        }
                        if (j.settings[a].sTableId == this.id) {
                            j.settings.splice(a, 1);
                            break
                        }
                    }
                    if (null === c || "" === c) this.id = c = "DataTables_Table_" + j.ext._oExternConfig.iNextUnique++;
                    var g = h.extend(!0, {}, j.models.oSettings, {
                        nTable: this,
                        oApi: ca.oApi,
                        oInit: e,
                        sDestroyWidth: h(this).width(),
                        sInstance: c,
                        sTableId: c
                    });
                    j.settings.push(g);
                    g.oInstance = 1 === ca.length ? ca : h(this).dataTable();
                    e || (e = {});
                    e.oLanguage && pa(e.oLanguage);
                    e = Ta(h.extend(!0, {}, j.defaults), e);
                    p(g.oFeatures, e, "bPaginate");
                    p(g.oFeatures, e, "bLengthChange");
                    p(g.oFeatures, e, "bFilter");
                    p(g.oFeatures, e, "bSort");
                    p(g.oFeatures, e, "bInfo");
                    p(g.oFeatures, e, "bProcessing");
                    p(g.oFeatures, e, "bAutoWidth");
                    p(g.oFeatures, e, "bSortClasses");
                    p(g.oFeatures, e, "bServerSide");
                    p(g.oFeatures, e, "bDeferRender");
                    p(g.oScroll, e, "sScrollX", "sX");
                    p(g.oScroll, e, "sScrollXInner", "sXInner");
                    p(g.oScroll, e, "sScrollY", "sY");
                    p(g.oScroll, e, "bScrollCollapse", "bCollapse");
                    p(g.oScroll, e, "bScrollInfinite", "bInfinite");
                    p(g.oScroll, e, "iScrollLoadGap", "iLoadGap");
                    p(g.oScroll, e, "bScrollAutoCss", "bAutoCss");
                    p(g, e, "asStripeClasses");
                    p(g, e, "asStripClasses", "asStripeClasses");
                    p(g, e, "fnServerData");
                    p(g, e, "fnFormatNumber");
                    p(g, e, "sServerMethod");
                    p(g, e, "aaSorting");
                    p(g,
                        e, "aaSortingFixed");
                    p(g, e, "aLengthMenu");
                    p(g, e, "sPaginationType");
                    p(g, e, "sAjaxSource");
                    p(g, e, "sAjaxDataProp");
                    p(g, e, "iCookieDuration");
                    p(g, e, "sCookiePrefix");
                    p(g, e, "sDom");
                    p(g, e, "bSortCellsTop");
                    p(g, e, "iTabIndex");
                    p(g, e, "oSearch", "oPreviousSearch");
                    p(g, e, "aoSearchCols", "aoPreSearchCols");
                    p(g, e, "iDisplayLength", "_iDisplayLength");
                    p(g, e, "bJQueryUI", "bJUI");
                    p(g, e, "fnCookieCallback");
                    p(g, e, "fnStateLoad");
                    p(g, e, "fnStateSave");
                    p(g.oLanguage, e, "fnInfoCallback");
                    z(g, "aoDrawCallback", e.fnDrawCallback, "user");
                    z(g, "aoServerParams", e.fnServerParams, "user");
                    z(g, "aoStateSaveParams", e.fnStateSaveParams, "user");
                    z(g, "aoStateLoadParams", e.fnStateLoadParams, "user");
                    z(g, "aoStateLoaded", e.fnStateLoaded, "user");
                    z(g, "aoRowCallback", e.fnRowCallback, "user");
                    z(g, "aoRowCreatedCallback", e.fnCreatedRow, "user");
                    z(g, "aoHeaderCallback", e.fnHeaderCallback, "user");
                    z(g, "aoFooterCallback", e.fnFooterCallback, "user");
                    z(g, "aoInitComplete", e.fnInitComplete, "user");
                    z(g, "aoPreDrawCallback", e.fnPreDrawCallback, "user");
                    g.oFeatures.bServerSide &&
                        g.oFeatures.bSort && g.oFeatures.bSortClasses ? z(g, "aoDrawCallback", P, "server_side_sort_classes") : g.oFeatures.bDeferRender && z(g, "aoDrawCallback", P, "defer_sort_classes");
                    e.bJQueryUI ? (h.extend(g.oClasses, j.ext.oJUIClasses), e.sDom === j.defaults.sDom && "lfrtip" === j.defaults.sDom && (g.sDom = '<"H"lfr>t<"F"ip>')) : h.extend(g.oClasses, j.ext.oStdClasses);
                    h(this).addClass(g.oClasses.sTable);
                    if ("" !== g.oScroll.sX || "" !== g.oScroll.sY) g.oScroll.iBarWidth = Qa();
                    g.iInitDisplayStart === n && (g.iInitDisplayStart = e.iDisplayStart,
                        g._iDisplayStart = e.iDisplayStart);
                    e.bStateSave && (g.oFeatures.bStateSave = !0, Sa(g, e), z(g, "aoDrawCallback", ra, "state_save"));
                    null !== e.iDeferLoading && (g.bDeferLoading = !0, a = h.isArray(e.iDeferLoading), g._iRecordsDisplay = a ? e.iDeferLoading[0] : e.iDeferLoading, g._iRecordsTotal = a ? e.iDeferLoading[1] : e.iDeferLoading);
                    null !== e.aaData && (f = !0);
                    "" !== e.oLanguage.sUrl ? (g.oLanguage.sUrl = e.oLanguage.sUrl, h.getJSON(g.oLanguage.sUrl, null, function(a) {
                        pa(a);
                        h.extend(true, g.oLanguage, e.oLanguage, a);
                        ba(g)
                    }), i = !0) : h.extend(!0,
                        g.oLanguage, e.oLanguage);
                    null === e.asStripeClasses && (g.asStripeClasses = [g.oClasses.sStripeOdd, g.oClasses.sStripeEven]);
                    b = g.asStripeClasses.length;
                    g.asDestroyStripes = [];
                    if (b) {
                        c = !1;
                        d = h(this).children("tbody").children("tr:lt(" + b + ")");
                        for (a = 0; a < b; a++) d.hasClass(g.asStripeClasses[a]) && (c = !0, g.asDestroyStripes.push(g.asStripeClasses[a]));
                        c && d.removeClass(g.asStripeClasses.join(" "))
                    }
                    c = [];
                    a = this.getElementsByTagName("thead");
                    0 !== a.length && (V(g.aoHeader, a[0]), c = N(g));
                    if (null === e.aoColumns) {
                        d = [];
                        a = 0;
                        for (b =
                            c.length; a < b; a++) d.push(null)
                    } else d = e.aoColumns;
                    a = 0;
                    for (b = d.length; a < b; a++) e.saved_aoColumns !== n && e.saved_aoColumns.length == b && (null === d[a] && (d[a] = {}), d[a].bVisible = e.saved_aoColumns[a].bVisible), o(g, c ? c[a] : null);
                    ta(g, e.aoColumnDefs, d, function(a, b) {
                        m(g, a, b)
                    });
                    a = 0;
                    for (b = g.aaSorting.length; a < b; a++) {
                        g.aaSorting[a][0] >= g.aoColumns.length && (g.aaSorting[a][0] = 0);
                        var k = g.aoColumns[g.aaSorting[a][0]];
                        g.aaSorting[a][2] === n && (g.aaSorting[a][2] = 0);
                        e.aaSorting === n && g.saved_aaSorting === n && (g.aaSorting[a][1] =
                            k.asSorting[0]);
                        c = 0;
                        for (d = k.asSorting.length; c < d; c++)
                            if (g.aaSorting[a][1] == k.asSorting[c]) {
                                g.aaSorting[a][2] = c;
                                break
                            }
                    }
                    P(g);
                    Ua(g);
                    a = h(this).children("caption").each(function() {
                        this._captionSide = h(this).css("caption-side")
                    });
                    b = h(this).children("thead");
                    0 === b.length && (b = [l.createElement("thead")], this.appendChild(b[0]));
                    g.nTHead = b[0];
                    b = h(this).children("tbody");
                    0 === b.length && (b = [l.createElement("tbody")], this.appendChild(b[0]));
                    g.nTBody = b[0];
                    g.nTBody.setAttribute("role", "alert");
                    g.nTBody.setAttribute("aria-live",
                        "polite");
                    g.nTBody.setAttribute("aria-relevant", "all");
                    b = h(this).children("tfoot");
                    if (0 === b.length && 0 < a.length && ("" !== g.oScroll.sX || "" !== g.oScroll.sY)) b = [l.createElement("tfoot")], this.appendChild(b[0]);
                    0 < b.length && (g.nTFoot = b[0], V(g.aoFooter, g.nTFoot));
                    if (f)
                        for (a = 0; a < e.aaData.length; a++) H(g, e.aaData[a]);
                    else ua(g);
                    g.aiDisplay = g.aiDisplayMaster.slice();
                    g.bInitialised = !0;
                    !1 === i && ba(g)
                }
            });
            ca = null;
            return this
        };
        j.fnVersionCheck = function(e) {
            for (var h = function(e, h) {
                        for (; e.length < h;) e += "0";
                        return e
                    }, m = j.ext.sVersion.split("."),
                    e = e.split("."), k = "", n = "", l = 0, t = e.length; l < t; l++) k += h(m[l], 3), n += h(e[l], 3);
            return parseInt(k, 10) >= parseInt(n, 10)
        };
        j.fnIsDataTable = function(e) {
            for (var h = j.settings, m = 0; m < h.length; m++)
                if (h[m].nTable === e || h[m].nScrollHead === e || h[m].nScrollFoot === e) return !0;
            return !1
        };
        j.fnTables = function(e) {
            var o = [];
            jQuery.each(j.settings, function(j, k) {
                (!e || !0 === e && h(k.nTable).is(":visible")) && o.push(k.nTable)
            });
            return o
        };
        j.version = "1.9.4";
        j.settings = [];
        j.models = {};
        j.models.ext = {
            afnFiltering: [],
            afnSortData: [],
            aoFeatures: [],
            aTypes: [],
            fnVersionCheck: j.fnVersionCheck,
            iApiIndex: 0,
            ofnSearch: {},
            oApi: {},
            oStdClasses: {},
            oJUIClasses: {},
            oPagination: {},
            oSort: {},
            sVersion: j.version,
            sErrMode: "alert",
            _oExternConfig: {
                iNextUnique: 0
            }
        };
        j.models.oSearch = {
            bCaseInsensitive: !0,
            sSearch: "",
            bRegex: !1,
            bSmart: !0
        };
        j.models.oRow = {
            nTr: null,
            _aData: [],
            _aSortData: [],
            _anHidden: [],
            _sRowStripe: ""
        };
        j.models.oColumn = {
            aDataSort: null,
            asSorting: null,
            bSearchable: null,
            bSortable: null,
            bUseRendered: null,
            bVisible: null,
            _bAutoType: !0,
            fnCreatedCell: null,
            fnGetData: null,
            fnRender: null,
            fnSetData: null,
            mData: null,
            mRender: null,
            nTh: null,
            nTf: null,
            sClass: null,
            sContentPadding: null,
            sDefaultContent: null,
            sName: null,
            sSortDataType: "std",
            sSortingClass: null,
            sSortingClassJUI: null,
            sTitle: null,
            sType: null,
            sWidth: null,
            sWidthOrig: null
        };
        j.defaults = {
            aaData: null,
            aaSorting: [
                [0, "asc"]
            ],
            aaSortingFixed: null,
            aLengthMenu: [10, 25, 50, 100],
            aoColumns: null,
            aoColumnDefs: null,
            aoSearchCols: [],
            asStripeClasses: null,
            bAutoWidth: !0,
            bDeferRender: !1,
            bDestroy: !1,
            bFilter: !0,
            bInfo: !0,
            bJQueryUI: !1,
            bLengthChange: !0,
            bPaginate: !0,
            bProcessing: !1,
            bRetrieve: !1,
            bScrollAutoCss: !0,
            bScrollCollapse: !1,
            bScrollInfinite: !1,
            bServerSide: !1,
            bSort: !0,
            bSortCellsTop: !1,
            bSortClasses: !0,
            bStateSave: !1,
            fnCookieCallback: null,
            fnCreatedRow: null,
            fnDrawCallback: null,
            fnFooterCallback: null,
            fnFormatNumber: function(e) {
                if (1E3 > e) return e;
                for (var h = e + "", e = h.split(""), j = "", h = h.length, k = 0; k < h; k++) 0 === k % 3 && 0 !== k && (j = this.oLanguage.sInfoThousands + j), j = e[h - k - 1] + j;
                return j
            },
            fnHeaderCallback: null,
            fnInfoCallback: null,
            fnInitComplete: null,
            fnPreDrawCallback: null,
            fnRowCallback: null,
            fnServerData: function(e, j, m, k) {
                k.jqXHR = h.ajax({
                    url: e,
                    data: j,
                    success: function(e) {
                        e.sError && k.oApi._fnLog(k, 0, e.sError);
                        h(k.oInstance).trigger("xhr", [k, e]);
                        m(e)
                    },
                    dataType: "json",
                    cache: !1,
                    type: k.sServerMethod,
                    error: function(e, h) {
                        "parsererror" == h && k.oApi._fnLog(k, 0, "DataTables warning: JSON data from server could not be parsed. This is caused by a JSON formatting error.")
                    }
                })
            },
            fnServerParams: null,
            fnStateLoad: function(e) {
                var e = this.oApi._fnReadCookie(e.sCookiePrefix + e.sInstance),
                    j;
                try {
                    j =
                        "function" === typeof h.parseJSON ? h.parseJSON(e) : eval("(" + e + ")")
                } catch (m) {
                    j = null
                }
                return j
            },
            fnStateLoadParams: null,
            fnStateLoaded: null,
            fnStateSave: function(e, h) {
                this.oApi._fnCreateCookie(e.sCookiePrefix + e.sInstance, this.oApi._fnJsonString(h), e.iCookieDuration, e.sCookiePrefix, e.fnCookieCallback)
            },
            fnStateSaveParams: null,
            iCookieDuration: 7200,
            iDeferLoading: null,
            iDisplayLength: 10,
            iDisplayStart: 0,
            iScrollLoadGap: 100,
            iTabIndex: 0,
            oLanguage: {
                oAria: {
                    sSortAscending: ": activate to sort column ascending",
                    sSortDescending: ": activate to sort column descending"
                },
                oPaginate: {
                    sFirst: "First",
                    sLast: "Last",
                    sNext: "Next",
                    sPrevious: "Previous"
                },
                sEmptyTable: "No data available in table",
                sInfo: "Showing _START_ to _END_ of _TOTAL_ entries",
                sInfoEmpty: "Showing 0 to 0 of 0 entries",
                sInfoFiltered: "(filtered from _MAX_ total entries)",
                sInfoPostFix: "",
                sInfoThousands: ",",
                sLengthMenu: "Show _MENU_ entries",
                sLoadingRecords: "Loading...",
                sProcessing: "Processing...",
                sSearch: "Search:",
                sUrl: "",
                sZeroRecords: "No matching records found"
            },
            oSearch: h.extend({}, j.models.oSearch),
            sAjaxDataProp: "aaData",
            sAjaxSource: null,
            sCookiePrefix: "SpryMedia_DataTables_",
            sDom: "lfrtip",
            sPaginationType: "two_button",
            sScrollX: "",
            sScrollXInner: "",
            sScrollY: "",
            sServerMethod: "GET"
        };
        j.defaults.columns = {
            aDataSort: null,
            asSorting: ["asc", "desc"],
            bSearchable: !0,
            bSortable: !0,
            bUseRendered: !0,
            bVisible: !0,
            fnCreatedCell: null,
            fnRender: null,
            iDataSort: -1,
            mData: null,
            mRender: null,
            sCellType: "td",
            sClass: "",
            sContentPadding: "",
            sDefaultContent: null,
            sName: "",
            sSortDataType: "std",
            sTitle: null,
            sType: null,
            sWidth: null
        };
        j.models.oSettings = {
            oFeatures: {
                bAutoWidth: null,
                bDeferRender: null,
                bFilter: null,
                bInfo: null,
                bLengthChange: null,
                bPaginate: null,
                bProcessing: null,
                bServerSide: null,
                bSort: null,
                bSortClasses: null,
                bStateSave: null
            },
            oScroll: {
                bAutoCss: null,
                bCollapse: null,
                bInfinite: null,
                iBarWidth: 0,
                iLoadGap: null,
                sX: null,
                sXInner: null,
                sY: null
            },
            oLanguage: {
                fnInfoCallback: null
            },
            oBrowser: {
                bScrollOversize: !1
            },
            aanFeatures: [],
            aoData: [],
            aiDisplay: [],
            aiDisplayMaster: [],
            aoColumns: [],
            aoHeader: [],
            aoFooter: [],
            asDataSearch: [],
            oPreviousSearch: {},
            aoPreSearchCols: [],
            aaSorting: null,
            aaSortingFixed: null,
            asStripeClasses: null,
            asDestroyStripes: [],
            sDestroyWidth: 0,
            aoRowCallback: [],
            aoHeaderCallback: [],
            aoFooterCallback: [],
            aoDrawCallback: [],
            aoRowCreatedCallback: [],
            aoPreDrawCallback: [],
            aoInitComplete: [],
            aoStateSaveParams: [],
            aoStateLoadParams: [],
            aoStateLoaded: [],
            sTableId: "",
            nTable: null,
            nTHead: null,
            nTFoot: null,
            nTBody: null,
            nTableWrapper: null,
            bDeferLoading: !1,
            bInitialised: !1,
            aoOpenRows: [],
            sDom: null,
            sPaginationType: "two_button",
            iCookieDuration: 0,
            sCookiePrefix: "",
            fnCookieCallback: null,
            aoStateSave: [],
            aoStateLoad: [],
            oLoadedState: null,
            sAjaxSource: null,
            sAjaxDataProp: null,
            bAjaxDataGet: !0,
            jqXHR: null,
            fnServerData: null,
            aoServerParams: [],
            sServerMethod: null,
            fnFormatNumber: null,
            aLengthMenu: null,
            iDraw: 0,
            bDrawing: !1,
            iDrawError: -1,
            _iDisplayLength: 10,
            _iDisplayStart: 0,
            _iDisplayEnd: 10,
            _iRecordsTotal: 0,
            _iRecordsDisplay: 0,
            bJUI: null,
            oClasses: {},
            bFiltered: !1,
            bSorted: !1,
            bSortCellsTop: null,
            oInit: null,
            aoDestroyCallback: [],
            fnRecordsTotal: function() {
                return this.oFeatures.bServerSide ? parseInt(this._iRecordsTotal, 10) : this.aiDisplayMaster.length
            },
            fnRecordsDisplay: function() {
                return this.oFeatures.bServerSide ? parseInt(this._iRecordsDisplay, 10) : this.aiDisplay.length
            },
            fnDisplayEnd: function() {
                return this.oFeatures.bServerSide ? !1 === this.oFeatures.bPaginate || -1 == this._iDisplayLength ? this._iDisplayStart + this.aiDisplay.length : Math.min(this._iDisplayStart + this._iDisplayLength, this._iRecordsDisplay) : this._iDisplayEnd
            },
            oInstance: null,
            sInstance: null,
            iTabIndex: 0,
            nScrollHead: null,
            nScrollFoot: null
        };
        j.ext = h.extend(!0, {}, j.models.ext);
        h.extend(j.ext.oStdClasses, {
            sTable: "dataTable",
            sPagePrevEnabled: "paginate_enabled_previous",
            sPagePrevDisabled: "paginate_disabled_previous",
            sPageNextEnabled: "paginate_enabled_next",
            sPageNextDisabled: "paginate_disabled_next",
            sPageJUINext: "",
            sPageJUIPrev: "",
            sPageButton: "paginate_button",
            sPageButtonActive: "paginate_active",
            sPageButtonStaticDisabled: "paginate_button paginate_button_disabled",
            sPageFirst: "first",
            sPagePrevious: "previous",
            sPageNext: "next",
            sPageLast: "last",
            sStripeOdd: "odd",
            sStripeEven: "even",
            sRowEmpty: "dataTables_empty",
            sWrapper: "dataTables_wrapper",
            sFilter: "dataTables_filter",
            sInfo: "dataTables_info",
            sPaging: "dataTables_paginate paging_",
            sLength: "dataTables_length",
            sProcessing: "dataTables_processing",
            sSortAsc: "sorting_asc",
            sSortDesc: "sorting_desc",
            sSortable: "sorting",
            sSortableAsc: "sorting_asc_disabled",
            sSortableDesc: "sorting_desc_disabled",
            sSortableNone: "sorting_disabled",
            sSortColumn: "sorting_",
            sSortJUIAsc: "",
            sSortJUIDesc: "",
            sSortJUI: "",
            sSortJUIAscAllowed: "",
            sSortJUIDescAllowed: "",
            sSortJUIWrapper: "",
            sSortIcon: "",
            sScrollWrapper: "dataTables_scroll",
            sScrollHead: "dataTables_scrollHead",
            sScrollHeadInner: "dataTables_scrollHeadInner",
            sScrollBody: "dataTables_scrollBody",
            sScrollFoot: "dataTables_scrollFoot",
            sScrollFootInner: "dataTables_scrollFootInner",
            sFooterTH: "",
            sJUIHeader: "",
            sJUIFooter: ""
        });
        h.extend(j.ext.oJUIClasses, j.ext.oStdClasses, {
            sPagePrevEnabled: "fg-button ui-button ui-state-default ui-corner-left",
            sPagePrevDisabled: "fg-button ui-button ui-state-default ui-corner-left ui-state-disabled",
            sPageNextEnabled: "fg-button ui-button ui-state-default ui-corner-right",
            sPageNextDisabled: "fg-button ui-button ui-state-default ui-corner-right ui-state-disabled",
            sPageJUINext: "ui-icon ui-icon-circle-arrow-e",
            sPageJUIPrev: "ui-icon ui-icon-circle-arrow-w",
            sPageButton: "fg-button ui-button ui-state-default",
            sPageButtonActive: "fg-button ui-button ui-state-default ui-state-disabled",
            sPageButtonStaticDisabled: "fg-button ui-button ui-state-default ui-state-disabled",
            sPageFirst: "first ui-corner-tl ui-corner-bl",
            sPageLast: "last ui-corner-tr ui-corner-br",
            sPaging: "dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_",
            sSortAsc: "ui-state-default",
            sSortDesc: "ui-state-default",
            sSortable: "ui-state-default",
            sSortableAsc: "ui-state-default",
            sSortableDesc: "ui-state-default",
            sSortableNone: "ui-state-default",
            sSortJUIAsc: "css_right ui-icon ui-icon-triangle-1-n",
            sSortJUIDesc: "css_right ui-icon ui-icon-triangle-1-s",
            sSortJUI: "css_right ui-icon ui-icon-carat-2-n-s",
            sSortJUIAscAllowed: "css_right ui-icon ui-icon-carat-1-n",
            sSortJUIDescAllowed: "css_right ui-icon ui-icon-carat-1-s",
            sSortJUIWrapper: "DataTables_sort_wrapper",
            sSortIcon: "DataTables_sort_icon",
            sScrollHead: "dataTables_scrollHead ui-state-default",
            sScrollFoot: "dataTables_scrollFoot ui-state-default",
            sFooterTH: "ui-state-default",
            sJUIHeader: "fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix",
            sJUIFooter: "fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"
        });
        h.extend(j.ext.oPagination, {
            two_button: {
                fnInit: function(e, j, m) {
                    var k = e.oLanguage.oPaginate,
                        n = function(h) {
                            e.oApi._fnPageChange(e, h.data.action) && m(e)
                        },
                        k = !e.bJUI ? '<a class="' +
                        e.oClasses.sPagePrevDisabled + '" tabindex="' + e.iTabIndex + '" role="button">' + k.sPrevious + '</a><a class="' + e.oClasses.sPageNextDisabled + '" tabindex="' + e.iTabIndex + '" role="button">' + k.sNext + "</a>" : '<a class="' + e.oClasses.sPagePrevDisabled + '" tabindex="' + e.iTabIndex + '" role="button"><span class="' + e.oClasses.sPageJUIPrev + '"></span></a><a class="' + e.oClasses.sPageNextDisabled + '" tabindex="' + e.iTabIndex + '" role="button"><span class="' + e.oClasses.sPageJUINext + '"></span></a>';
                    h(j).append(k);
                    var l = h("a", j),
                        k = l[0],
                        l = l[1];
                    e.oApi._fnBindAction(k, {
                        action: "previous"
                    }, n);
                    e.oApi._fnBindAction(l, {
                        action: "next"
                    }, n);
                    e.aanFeatures.p || (j.id = e.sTableId + "_paginate", k.id = e.sTableId + "_previous", l.id = e.sTableId + "_next", k.setAttribute("aria-controls", e.sTableId), l.setAttribute("aria-controls", e.sTableId))
                },
                fnUpdate: function(e) {
                    if (e.aanFeatures.p)
                        for (var h = e.oClasses, j = e.aanFeatures.p, k, l = 0, n = j.length; l < n; l++)
                            if (k = j[l].firstChild) k.className = 0 === e._iDisplayStart ? h.sPagePrevDisabled : h.sPagePrevEnabled, k = k.nextSibling,
                                k.className = e.fnDisplayEnd() == e.fnRecordsDisplay() ? h.sPageNextDisabled : h.sPageNextEnabled
                }
            },
            iFullNumbersShowPages: 5,
            full_numbers: {
                fnInit: function(e, j, m) {
                    var k = e.oLanguage.oPaginate,
                        l = e.oClasses,
                        n = function(h) {
                            e.oApi._fnPageChange(e, h.data.action) && m(e)
                        };
                    h(j).append('<a  tabindex="' + e.iTabIndex + '" class="' + l.sPageButton + " " + l.sPageFirst + '">' + k.sFirst + '</a><a  tabindex="' + e.iTabIndex + '" class="' + l.sPageButton + " " + l.sPagePrevious + '">' + k.sPrevious + '</a><span></span><a tabindex="' + e.iTabIndex + '" class="' +
                        l.sPageButton + " " + l.sPageNext + '">' + k.sNext + '</a><a tabindex="' + e.iTabIndex + '" class="' + l.sPageButton + " " + l.sPageLast + '">' + k.sLast + "</a>");
                    var t = h("a", j),
                        k = t[0],
                        l = t[1],
                        r = t[2],
                        t = t[3];
                    e.oApi._fnBindAction(k, {
                        action: "first"
                    }, n);
                    e.oApi._fnBindAction(l, {
                        action: "previous"
                    }, n);
                    e.oApi._fnBindAction(r, {
                        action: "next"
                    }, n);
                    e.oApi._fnBindAction(t, {
                        action: "last"
                    }, n);
                    e.aanFeatures.p || (j.id = e.sTableId + "_paginate", k.id = e.sTableId + "_first", l.id = e.sTableId + "_previous", r.id = e.sTableId + "_next", t.id = e.sTableId + "_last")
                },
                fnUpdate: function(e, o) {
                    if (e.aanFeatures.p) {
                        var m = j.ext.oPagination.iFullNumbersShowPages,
                            k = Math.floor(m / 2),
                            l = Math.ceil(e.fnRecordsDisplay() / e._iDisplayLength),
                            n = Math.ceil(e._iDisplayStart / e._iDisplayLength) + 1,
                            t = "",
                            r, B = e.oClasses,
                            u, M = e.aanFeatures.p,
                            L = function(h) {
                                e.oApi._fnBindAction(this, {
                                    page: h + r - 1
                                }, function(h) {
                                    e.oApi._fnPageChange(e, h.data.page);
                                    o(e);
                                    h.preventDefault()
                                })
                            }; - 1 === e._iDisplayLength ? n = k = r = 1 : l < m ? (r = 1, k = l) : n <= k ? (r = 1, k = m) : n >= l - k ? (r = l - m + 1, k = l) : (r = n - Math.ceil(m / 2) + 1, k = r + m - 1);
                        for (m = r; m <= k; m++) t +=
                            n !== m ? '<a tabindex="' + e.iTabIndex + '" class="' + B.sPageButton + '">' + e.fnFormatNumber(m) + "</a>" : '<a tabindex="' + e.iTabIndex + '" class="' + B.sPageButtonActive + '">' + e.fnFormatNumber(m) + "</a>";
                        m = 0;
                        for (k = M.length; m < k; m++) u = M[m], u.hasChildNodes() && (h("span:eq(0)", u).html(t).children("a").each(L), u = u.getElementsByTagName("a"), u = [u[0], u[1], u[u.length - 2], u[u.length - 1]], h(u).removeClass(B.sPageButton + " " + B.sPageButtonActive + " " + B.sPageButtonStaticDisabled), h([u[0], u[1]]).addClass(1 == n ? B.sPageButtonStaticDisabled :
                            B.sPageButton), h([u[2], u[3]]).addClass(0 === l || n === l || -1 === e._iDisplayLength ? B.sPageButtonStaticDisabled : B.sPageButton))
                    }
                }
            }
        });
        h.extend(j.ext.oSort, {
            "string-pre": function(e) {
                "string" != typeof e && (e = null !== e && e.toString ? e.toString() : "");
                return e.toLowerCase()
            },
            "string-asc": function(e, h) {
                return e < h ? -1 : e > h ? 1 : 0
            },
            "string-desc": function(e, h) {
                return e < h ? 1 : e > h ? -1 : 0
            },
            "html-pre": function(e) {
                return e.replace(/<.*?>/g, "").toLowerCase()
            },
            "html-asc": function(e, h) {
                return e < h ? -1 : e > h ? 1 : 0
            },
            "html-desc": function(e, h) {
                return e <
                    h ? 1 : e > h ? -1 : 0
            },
            "date-pre": function(e) {
                e = Date.parse(e);
                if (isNaN(e) || "" === e) e = Date.parse("01/01/1970 00:00:00");
                return e
            },
            "date-asc": function(e, h) {
                return e - h
            },
            "date-desc": function(e, h) {
                return h - e
            },
            "numeric-pre": function(e) {
                return "-" == e || "" === e ? 0 : 1 * e
            },
            "numeric-asc": function(e, h) {
                return e - h
            },
            "numeric-desc": function(e, h) {
                return h - e
            }
        });
        h.extend(j.ext.aTypes, [function(e) {
            if ("number" === typeof e) return "numeric";
            if ("string" !== typeof e) return null;
            var h, j = !1;
            h = e.charAt(0);
            if (-1 == "0123456789-".indexOf(h)) return null;
            for (var k = 1; k < e.length; k++) {
                h = e.charAt(k);
                if (-1 == "0123456789.".indexOf(h)) return null;
                if ("." == h) {
                    if (j) return null;
                    j = !0
                }
            }
            return "numeric"
        }, function(e) {
            var h = Date.parse(e);
            return null !== h && !isNaN(h) || "string" === typeof e && 0 === e.length ? "date" : null
        }, function(e) {
            return "string" === typeof e && -1 != e.indexOf("<") && -1 != e.indexOf(">") ? "html" : null
        }]);
        h.fn.DataTable = j;
        h.fn.dataTable = j;
        h.fn.dataTableSettings = j.settings;
        h.fn.dataTableExt = j.ext
    };
    "function" === typeof define && define.amd ? define(["jquery"], L) : jQuery && !jQuery.fn.dataTable &&
        L(jQuery)
})(window, document);

/*!
 * FullCalendar v1.6.4 & Google Calendar Extension
 * Docs & License: http://arshaw.com/fullcalendar/
 * (c) 2013 Adam Shaw
 */
(function(t, e) {
    function n(e) {
        t.extend(!0, Ce, e)
    }

    function r(n, r, c) {
        function u(t) {
            ae ? p() && (S(), M(t)) : f()
        }

        function f() {
            oe = r.theme ? "ui" : "fc", n.addClass("fc"), r.isRTL ? n.addClass("fc-rtl") : n.addClass("fc-ltr"), r.theme && n.addClass("ui-widget"), ae = t("<div class='fc-content' style='position:relative'/>").prependTo(n), ne = new a(ee, r), re = ne.render(), re && n.prepend(re), y(r.defaultView), r.handleWindowResize && t(window).resize(x), m() || v()
        }

        function v() {
            setTimeout(function() {
                !ie.start && m() && C()
            }, 0)
        }

        function h() {
            ie && (te("viewDestroy", ie, ie, ie.element), ie.triggerEventDestroy()), t(window).unbind("resize", x), ne.destroy(), ae.remove(), n.removeClass("fc fc-rtl ui-widget")
        }

        function p() {
            return n.is(":visible")
        }

        function m() {
            return t("body").is(":visible")
        }

        function y(t) {
            ie && t == ie.name || D(t)
        }

        function D(e) {
            he++, ie && (te("viewDestroy", ie, ie, ie.element), Y(), ie.triggerEventDestroy(), G(), ie.element.remove(), ne.deactivateButton(ie.name)), ne.activateButton(e), ie = new Se[e](t("<div class='fc-view fc-view-" + e + "' style='position:relative'/>").appendTo(ae), ee), C(), $(), he--
        }

        function C(t) {
            (!ie.start || t || ie.start > ge || ge >= ie.end) && p() && M(t)
        }

        function M(t) {
            he++, ie.start && (te("viewDestroy", ie, ie, ie.element), Y(), N()), G(), ie.render(ge, t || 0), T(), $(), (ie.afterRender || A)(), _(), P(), te("viewRender", ie, ie, ie.element), ie.trigger("viewDisplay", de), he--, z()
        }

        function E() {
            p() && (Y(), N(), S(), T(), F())
        }

        function S() {
            le = r.contentHeight ? r.contentHeight : r.height ? r.height - (re ? re.height() : 0) - R(ae) : Math.round(ae.width() / Math.max(r.aspectRatio, .5))
        }

        function T() {
            le === e && S(), he++, ie.setHeight(le), ie.setWidth(ae.width()), he--, se = n.outerWidth()
        }

        function x() {
            if (!he)
                if (ie.start) {
                    var t = ++ve;
                    setTimeout(function() {
                        t == ve && !he && p() && se != (se = n.outerWidth()) && (he++, E(), ie.trigger("windowResize", de), he--)
                    }, 200)
                } else v()
        }

        function k() {
            N(), W()
        }

        function H(t) {
            N(), F(t)
        }

        function F(t) {
            p() && (ie.setEventData(pe), ie.renderEvents(pe, t), ie.trigger("eventAfterAllRender"))
        }

        function N() {
            ie.triggerEventDestroy(), ie.clearEvents(), ie.clearEventData()
        }

        function z() {
            !r.lazyFetching || ue(ie.visStart, ie.visEnd) ? W() : F()
        }

        function W() {
            fe(ie.visStart, ie.visEnd)
        }

        function O(t) {
            pe = t, F()
        }

        function L(t) {
            H(t)
        }

        function _() {
            ne.updateTitle(ie.title)
        }

        function P() {
            var t = new Date;
            t >= ie.start && ie.end > t ? ne.disableButton("today") : ne.enableButton("today")
        }

        function q(t, n, r) {
            ie.select(t, n, r === e ? !0 : r)
        }

        function Y() {
            ie && ie.unselect()
        }

        function B() {
            C(-1)
        }

        function j() {
            C(1)
        }

        function I() {
            i(ge, -1), C()
        }

        function X() {
            i(ge, 1), C()
        }

        function J() {
            ge = new Date, C()
        }

        function V(t, e, n) {
            t instanceof Date ? ge = d(t) : g(ge, t, e, n), C()
        }

        function U(t, n, r) {
            t !== e && i(ge, t), n !== e && s(ge, n), r !== e && l(ge, r), C()
        }

        function Z() {
            return d(ge)
        }

        function G() {
            ae.css({
                width: "100%",
                height: ae.height(),
                overflow: "hidden"
            })
        }

        function $() {
            ae.css({
                width: "",
                height: "",
                overflow: ""
            })
        }

        function Q() {
            return ie
        }

        function K(t, n) {
            return n === e ? r[t] : (("height" == t || "contentHeight" == t || "aspectRatio" == t) && (r[t] = n, E()), e)
        }

        function te(t, n) {
            return r[t] ? r[t].apply(n || de, Array.prototype.slice.call(arguments, 2)) : e
        }
        var ee = this;
        ee.options = r, ee.render = u, ee.destroy = h, ee.refetchEvents = k, ee.reportEvents = O, ee.reportEventChange = L, ee.rerenderEvents = H, ee.changeView = y, ee.select = q, ee.unselect = Y, ee.prev = B, ee.next = j, ee.prevYear = I, ee.nextYear = X, ee.today = J, ee.gotoDate = V, ee.incrementDate = U, ee.formatDate = function(t, e) {
            return w(t, e, r)
        }, ee.formatDates = function(t, e, n) {
            return b(t, e, n, r)
        }, ee.getDate = Z, ee.getView = Q, ee.option = K, ee.trigger = te, o.call(ee, r, c);
        var ne, re, ae, oe, ie, se, le, ce, ue = ee.isFetchNeeded,
            fe = ee.fetchEvents,
            de = n[0],
            ve = 0,
            he = 0,
            ge = new Date,
            pe = [];
        g(ge, r.year, r.month, r.date), r.droppable && t(document).bind("dragstart", function(e, n) {
            var a = e.target,
                o = t(a);
            if (!o.parents(".fc").length) {
                var i = r.dropAccept;
                (t.isFunction(i) ? i.call(a, o) : o.is(i)) && (ce = a, ie.dragStart(ce, e, n))
            }
        }).bind("dragstop", function(t, e) {
            ce && (ie.dragStop(ce, t, e), ce = null)
        })
    }

    function a(n, r) {
        function a() {
            v = r.theme ? "ui" : "fc";
            var n = r.header;
            return n ? h = t("<table class='fc-header' style='width:100%'/>").append(t("<tr/>").append(i("left")).append(i("center")).append(i("right"))) : e
        }

        function o() {
            h.remove()
        }

        function i(e) {
            var a = t("<td class='fc-header-" + e + "'/>"),
                o = r.header[e];
            return o && t.each(o.split(" "), function(e) {
                e > 0 && a.append("<span class='fc-header-space'/>");
                var o;
                t.each(this.split(","), function(e, i) {
                    if ("title" == i) a.append("<span class='fc-header-title'><h2>&nbsp;</h2></span>"), o && o.addClass(v + "-corner-right"), o = null;
                    else {
                        var s;
                        if (n[i] ? s = n[i] : Se[i] && (s = function() {
                                u.removeClass(v + "-state-hover"), n.changeView(i)
                            }), s) {
                            var l = r.theme ? P(r.buttonIcons, i) : null,
                                c = P(r.buttonText, i),
                                u = t("<span class='fc-button fc-button-" + i + " " + v + "-state-default'>" + (l ? "<span class='fc-icon-wrap'><span class='ui-icon ui-icon-" + l + "'/>" + "</span>" : c) + "</span>").click(function() {
                                    u.hasClass(v + "-state-disabled") || s()
                                }).mousedown(function() {
                                    u.not("." + v + "-state-active").not("." + v + "-state-disabled").addClass(v + "-state-down")
                                }).mouseup(function() {
                                    u.removeClass(v + "-state-down")
                                }).hover(function() {
                                    u.not("." + v + "-state-active").not("." + v + "-state-disabled").addClass(v + "-state-hover")
                                }, function() {
                                    u.removeClass(v + "-state-hover").removeClass(v + "-state-down")
                                }).appendTo(a);
                            Y(u), o || u.addClass(v + "-corner-left"), o = u
                        }
                    }
                }), o && o.addClass(v + "-corner-right")
            }), a
        }

        function s(t) {
            h.find("h2").html(t)
        }

        function l(t) {
            h.find("span.fc-button-" + t).addClass(v + "-state-active")
        }

        function c(t) {
            h.find("span.fc-button-" + t).removeClass(v + "-state-active")
        }

        function u(t) {
            h.find("span.fc-button-" + t).addClass(v + "-state-disabled")
        }

        function f(t) {
            h.find("span.fc-button-" + t).removeClass(v + "-state-disabled")
        }
        var d = this;
        d.render = a, d.destroy = o, d.updateTitle = s, d.activateButton = l, d.deactivateButton = c, d.disableButton = u, d.enableButton = f;
        var v, h = t([])
    }

    function o(n, r) {
        function a(t, e) {
            return !E || E > t || e > S
        }

        function o(t, e) {
            E = t, S = e, W = [];
            var n = ++R,
                r = F.length;
            N = r;
            for (var a = 0; r > a; a++) i(F[a], n)
        }

        function i(e, r) {
            s(e, function(a) {
                if (r == R) {
                    if (a) {
                        n.eventDataTransform && (a = t.map(a, n.eventDataTransform)), e.eventDataTransform && (a = t.map(a, e.eventDataTransform));
                        for (var o = 0; a.length > o; o++) a[o].source = e, w(a[o]);
                        W = W.concat(a)
                    }
                    N--, N || k(W)
                }
            })
        }

        function s(r, a) {
            var o, i, l = Ee.sourceFetchers;
            for (o = 0; l.length > o; o++) {
                if (i = l[o](r, E, S, a), i === !0) return;
                if ("object" == typeof i) return s(i, a), e
            }
            var c = r.events;
            if (c) t.isFunction(c) ? (m(), c(d(E), d(S), function(t) {
                a(t), y()
            })) : t.isArray(c) ? a(c) : a();
            else {
                var u = r.url;
                if (u) {
                    var f, v = r.success,
                        h = r.error,
                        g = r.complete;
                    f = t.isFunction(r.data) ? r.data() : r.data;
                    var p = t.extend({}, f || {}),
                        w = X(r.startParam, n.startParam),
                        b = X(r.endParam, n.endParam);
                    w && (p[w] = Math.round(+E / 1e3)), b && (p[b] = Math.round(+S / 1e3)), m(), t.ajax(t.extend({}, Te, r, {
                        data: p,
                        success: function(e) {
                            e = e || [];
                            var n = I(v, this, arguments);
                            t.isArray(n) && (e = n), a(e)
                        },
                        error: function() {
                            I(h, this, arguments), a()
                        },
                        complete: function() {
                            I(g, this, arguments), y()
                        }
                    }))
                } else a()
            }
        }

        function l(t) {
            t = c(t), t && (N++, i(t, R))
        }

        function c(n) {
            return t.isFunction(n) || t.isArray(n) ? n = {
                events: n
            } : "string" == typeof n && (n = {
                url: n
            }), "object" == typeof n ? (b(n), F.push(n), n) : e
        }

        function u(e) {
            F = t.grep(F, function(t) {
                return !D(t, e)
            }), W = t.grep(W, function(t) {
                return !D(t.source, e)
            }), k(W)
        }

        function f(t) {
            var e, n, r = W.length,
                a = x().defaultEventEnd,
                o = t.start - t._start,
                i = t.end ? t.end - (t._end || a(t)) : 0;
            for (e = 0; r > e; e++) n = W[e], n._id == t._id && n != t && (n.start = new Date(+n.start + o), n.end = t.end ? n.end ? new Date(+n.end + i) : new Date(+a(n) + i) : null, n.title = t.title, n.url = t.url, n.allDay = t.allDay, n.className = t.className, n.editable = t.editable, n.color = t.color, n.backgroundColor = t.backgroundColor, n.borderColor = t.borderColor, n.textColor = t.textColor, w(n));
            w(t), k(W)
        }

        function v(t, e) {
            w(t), t.source || (e && (H.events.push(t), t.source = H), W.push(t)), k(W)
        }

        function h(e) {
            if (e) {
                if (!t.isFunction(e)) {
                    var n = e + "";
                    e = function(t) {
                        return t._id == n
                    }
                }
                W = t.grep(W, e, !0);
                for (var r = 0; F.length > r; r++) t.isArray(F[r].events) && (F[r].events = t.grep(F[r].events, e, !0))
            } else {
                W = [];
                for (var r = 0; F.length > r; r++) t.isArray(F[r].events) && (F[r].events = [])
            }
            k(W)
        }

        function g(e) {
            return t.isFunction(e) ? t.grep(W, e) : e ? (e += "", t.grep(W, function(t) {
                return t._id == e
            })) : W
        }

        function m() {
            z++ || T("loading", null, !0, x())
        }

        function y() {
            --z || T("loading", null, !1, x())
        }

        function w(t) {
            var r = t.source || {},
                a = X(r.ignoreTimezone, n.ignoreTimezone);
            t._id = t._id || (t.id === e ? "_fc" + xe++ : t.id + ""), t.date && (t.start || (t.start = t.date), delete t.date), t._start = d(t.start = p(t.start, a)), t.end = p(t.end, a), t.end && t.end <= t.start && (t.end = null), t._end = t.end ? d(t.end) : null, t.allDay === e && (t.allDay = X(r.allDayDefault, n.allDayDefault)), t.className ? "string" == typeof t.className && (t.className = t.className.split(/\s+/)) : t.className = []
        }

        function b(t) {
            t.className ? "string" == typeof t.className && (t.className = t.className.split(/\s+/)) : t.className = [];
            for (var e = Ee.sourceNormalizers, n = 0; e.length > n; n++) e[n](t)
        }

        function D(t, e) {
            return t && e && C(t) == C(e)
        }

        function C(t) {
            return ("object" == typeof t ? t.events || t.url : "") || t
        }
        var M = this;
        M.isFetchNeeded = a, M.fetchEvents = o, M.addEventSource = l, M.removeEventSource = u, M.updateEvent = f, M.renderEvent = v, M.removeEvents = h, M.clientEvents = g, M.normalizeEvent = w;
        for (var E, S, T = M.trigger, x = M.getView, k = M.reportEvents, H = {
                events: []
            }, F = [H], R = 0, N = 0, z = 0, W = [], A = 0; r.length > A; A++) c(r[A])
    }

    function i(t, e, n) {
        return t.setFullYear(t.getFullYear() + e), n || f(t), t
    }

    function s(t, e, n) {
        if (+t) {
            var r = t.getMonth() + e,
                a = d(t);
            for (a.setDate(1), a.setMonth(r), t.setMonth(r), n || f(t); t.getMonth() != a.getMonth();) t.setDate(t.getDate() + (a > t ? 1 : -1))
        }
        return t
    }

    function l(t, e, n) {
        if (+t) {
            var r = t.getDate() + e,
                a = d(t);
            a.setHours(9), a.setDate(r), t.setDate(r), n || f(t), c(t, a)
        }
        return t
    }

    function c(t, e) {
        if (+t)
            for (; t.getDate() != e.getDate();) t.setTime(+t + (e > t ? 1 : -1) * Fe)
    }

    function u(t, e) {
        return t.setMinutes(t.getMinutes() + e), t
    }

    function f(t) {
        return t.setHours(0), t.setMinutes(0), t.setSeconds(0), t.setMilliseconds(0), t
    }

    function d(t, e) {
        return e ? f(new Date(+t)) : new Date(+t)
    }

    function v() {
        var t, e = 0;
        do t = new Date(1970, e++, 1); while (t.getHours());
        return t
    }

    function h(t, e) {
        return Math.round((d(t, !0) - d(e, !0)) / He)
    }

    function g(t, n, r, a) {
        n !== e && n != t.getFullYear() && (t.setDate(1), t.setMonth(0), t.setFullYear(n)), r !== e && r != t.getMonth() && (t.setDate(1), t.setMonth(r)), a !== e && t.setDate(a)
    }

    function p(t, n) {
        return "object" == typeof t ? t : "number" == typeof t ? new Date(1e3 * t) : "string" == typeof t ? t.match(/^\d+(\.\d+)?$/) ? new Date(1e3 * parseFloat(t)) : (n === e && (n = !0), m(t, n) || (t ? new Date(t) : null)) : null
    }

    function m(t, e) {
        var n = t.match(/^([0-9]{4})(-([0-9]{2})(-([0-9]{2})([T ]([0-9]{2}):([0-9]{2})(:([0-9]{2})(\.([0-9]+))?)?(Z|(([-+])([0-9]{2})(:?([0-9]{2}))?))?)?)?)?$/);
        if (!n) return null;
        var r = new Date(n[1], 0, 1);
        if (e || !n[13]) {
            var a = new Date(n[1], 0, 1, 9, 0);
            n[3] && (r.setMonth(n[3] - 1), a.setMonth(n[3] - 1)), n[5] && (r.setDate(n[5]), a.setDate(n[5])), c(r, a), n[7] && r.setHours(n[7]), n[8] && r.setMinutes(n[8]), n[10] && r.setSeconds(n[10]), n[12] && r.setMilliseconds(1e3 * Number("0." + n[12])), c(r, a)
        } else if (r.setUTCFullYear(n[1], n[3] ? n[3] - 1 : 0, n[5] || 1), r.setUTCHours(n[7] || 0, n[8] || 0, n[10] || 0, n[12] ? 1e3 * Number("0." + n[12]) : 0), n[14]) {
            var o = 60 * Number(n[16]) + (n[18] ? Number(n[18]) : 0);
            o *= "-" == n[15] ? 1 : -1, r = new Date(+r + 1e3 * 60 * o)
        }
        return r
    }

    function y(t) {
        if ("number" == typeof t) return 60 * t;
        if ("object" == typeof t) return 60 * t.getHours() + t.getMinutes();
        var e = t.match(/(\d+)(?::(\d+))?\s*(\w+)?/);
        if (e) {
            var n = parseInt(e[1], 10);
            return e[3] && (n %= 12, "p" == e[3].toLowerCase().charAt(0) && (n += 12)), 60 * n + (e[2] ? parseInt(e[2], 10) : 0)
        }
    }

    function w(t, e, n) {
        return b(t, null, e, n)
    }

    function b(t, e, n, r) {
        r = r || Ce;
        var a, o, i, s, l = t,
            c = e,
            u = n.length,
            f = "";
        for (a = 0; u > a; a++)
            if (o = n.charAt(a), "'" == o) {
                for (i = a + 1; u > i; i++)
                    if ("'" == n.charAt(i)) {
                        l && (f += i == a + 1 ? "'" : n.substring(a + 1, i), a = i);
                        break
                    }
            } else if ("(" == o) {
            for (i = a + 1; u > i; i++)
                if (")" == n.charAt(i)) {
                    var d = w(l, n.substring(a + 1, i), r);
                    parseInt(d.replace(/\D/, ""), 10) && (f += d), a = i;
                    break
                }
        } else if ("[" == o) {
            for (i = a + 1; u > i; i++)
                if ("]" == n.charAt(i)) {
                    var v = n.substring(a + 1, i),
                        d = w(l, v, r);
                    d != w(c, v, r) && (f += d), a = i;
                    break
                }
        } else if ("{" == o) l = e, c = t;
        else if ("}" == o) l = t, c = e;
        else {
            for (i = u; i > a; i--)
                if (s = Ne[n.substring(a, i)]) {
                    l && (f += s(l, r)), a = i - 1;
                    break
                }
            i == a && l && (f += o)
        }
        return f
    }

    function D(t) {
        var e, n = new Date(t.getTime());
        return n.setDate(n.getDate() + 4 - (n.getDay() || 7)), e = n.getTime(), n.setMonth(0), n.setDate(1), Math.floor(Math.round((e - n) / 864e5) / 7) + 1
    }

    function C(t) {
        return t.end ? M(t.end, t.allDay) : l(d(t.start), 1)
    }

    function M(t, e) {
        return t = d(t), e || t.getHours() || t.getMinutes() ? l(t, 1) : f(t)
    }

    function E(n, r, a) {
        n.unbind("mouseover").mouseover(function(n) {
            for (var o, i, s, l = n.target; l != this;) o = l, l = l.parentNode;
            (i = o._fci) !== e && (o._fci = e, s = r[i], a(s.event, s.element, s), t(n.target).trigger(n)), n.stopPropagation()
        })
    }

    function S(e, n, r) {
        for (var a, o = 0; e.length > o; o++) a = t(e[o]), a.width(Math.max(0, n - x(a, r)))
    }

    function T(e, n, r) {
        for (var a, o = 0; e.length > o; o++) a = t(e[o]), a.height(Math.max(0, n - R(a, r)))
    }

    function x(t, e) {
        return k(t) + F(t) + (e ? H(t) : 0)
    }

    function k(e) {
        return (parseFloat(t.css(e[0], "paddingLeft", !0)) || 0) + (parseFloat(t.css(e[0], "paddingRight", !0)) || 0)
    }

    function H(e) {
        return (parseFloat(t.css(e[0], "marginLeft", !0)) || 0) + (parseFloat(t.css(e[0], "marginRight", !0)) || 0)
    }

    function F(e) {
        return (parseFloat(t.css(e[0], "borderLeftWidth", !0)) || 0) + (parseFloat(t.css(e[0], "borderRightWidth", !0)) || 0)
    }

    function R(t, e) {
        return N(t) + W(t) + (e ? z(t) : 0)
    }

    function N(e) {
        return (parseFloat(t.css(e[0], "paddingTop", !0)) || 0) + (parseFloat(t.css(e[0], "paddingBottom", !0)) || 0)
    }

    function z(e) {
        return (parseFloat(t.css(e[0], "marginTop", !0)) || 0) + (parseFloat(t.css(e[0], "marginBottom", !0)) || 0)
    }

    function W(e) {
        return (parseFloat(t.css(e[0], "borderTopWidth", !0)) || 0) + (parseFloat(t.css(e[0], "borderBottomWidth", !0)) || 0)
    }

    function A() {}

    function O(t, e) {
        return t - e
    }

    function L(t) {
        return Math.max.apply(Math, t)
    }

    function _(t) {
        return (10 > t ? "0" : "") + t
    }

    function P(t, n) {
        if (t[n] !== e) return t[n];
        for (var r, a = n.split(/(?=[A-Z])/), o = a.length - 1; o >= 0; o--)
            if (r = t[a[o].toLowerCase()], r !== e) return r;
        return t[""]
    }

    function q(t) {
        return t.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/'/g, "&#039;").replace(/"/g, "&quot;").replace(/\n/g, "<br />")
    }

    function Y(t) {
        t.attr("unselectable", "on").css("MozUserSelect", "none").bind("selectstart.ui", function() {
            return !1
        })
    }

    function B(t) {
        t.children().removeClass("fc-first fc-last").filter(":first-child").addClass("fc-first").end().filter(":last-child").addClass("fc-last")
    }

    function j(t, e) {
        var n = t.source || {},
            r = t.color,
            a = n.color,
            o = e("eventColor"),
            i = t.backgroundColor || r || n.backgroundColor || a || e("eventBackgroundColor") || o,
            s = t.borderColor || r || n.borderColor || a || e("eventBorderColor") || o,
            l = t.textColor || n.textColor || e("eventTextColor"),
            c = [];
        return i && c.push("background-color:" + i), s && c.push("border-color:" + s), l && c.push("color:" + l), c.join(";")
    }

    function I(e, n, r) {
        if (t.isFunction(e) && (e = [e]), e) {
            var a, o;
            for (a = 0; e.length > a; a++) o = e[a].apply(n, r) || o;
            return o
        }
    }

    function X() {
        for (var t = 0; arguments.length > t; t++)
            if (arguments[t] !== e) return arguments[t]
    }

    function J(t, e) {
        function n(t, e) {
            e && (s(t, e), t.setDate(1));
            var n = a("firstDay"),
                f = d(t, !0);
            f.setDate(1);
            var v = s(d(f), 1),
                g = d(f);
            l(g, -((g.getDay() - n + 7) % 7)), i(g);
            var p = d(v);
            l(p, (7 - p.getDay() + n) % 7), i(p, -1, !0);
            var m = c(),
                y = Math.round(h(p, g) / 7);
            "fixed" == a("weekMode") && (l(p, 7 * (6 - y)), y = 6), r.title = u(f, a("titleFormat")), r.start = f, r.end = v, r.visStart = g, r.visEnd = p, o(y, m, !0)
        }
        var r = this;
        r.render = n, Z.call(r, t, e, "month");
        var a = r.opt,
            o = r.renderBasic,
            i = r.skipHiddenDays,
            c = r.getCellsPerWeek,
            u = e.formatDate
    }

    function V(t, e) {
        function n(t, e) {
            e && l(t, 7 * e);
            var n = l(d(t), -((t.getDay() - a("firstDay") + 7) % 7)),
                u = l(d(n), 7),
                f = d(n);
            i(f);
            var v = d(u);
            i(v, -1, !0);
            var h = s();
            r.start = n, r.end = u, r.visStart = f, r.visEnd = v, r.title = c(f, l(d(v), -1), a("titleFormat")), o(1, h, !1)
        }
        var r = this;
        r.render = n, Z.call(r, t, e, "basicWeek");
        var a = r.opt,
            o = r.renderBasic,
            i = r.skipHiddenDays,
            s = r.getCellsPerWeek,
            c = e.formatDates
    }

    function U(t, e) {
        function n(t, e) {
            e && l(t, e), i(t, 0 > e ? -1 : 1);
            var n = d(t, !0),
                c = l(d(n), 1);
            r.title = s(t, a("titleFormat")), r.start = r.visStart = n, r.end = r.visEnd = c, o(1, 1, !1)
        }
        var r = this;
        r.render = n, Z.call(r, t, e, "basicDay");
        var a = r.opt,
            o = r.renderBasic,
            i = r.skipHiddenDays,
            s = e.formatDate
    }

    function Z(e, n, r) {
        function a(t, e, n) {
            ee = t, ne = e, re = n, o(), j || i(), s()
        }

        function o() {
            le = he("theme") ? "ui" : "fc", ce = he("columnFormat"), ue = he("weekNumbers"), de = he("weekNumberTitle"), ve = "iso" != he("weekNumberCalculation") ? "w" : "W"
        }

        function i() {
            Z = t("<div class='fc-event-container' style='position:absolute;z-index:8;top:0;left:0'/>").appendTo(e)
        }

        function s() {
            var n = c();
            L && L.remove(), L = t(n).appendTo(e), _ = L.find("thead"), P = _.find(".fc-day-header"), j = L.find("tbody"), I = j.find("tr"), X = j.find(".fc-day"), J = I.find("td:first-child"), V = I.eq(0).find(".fc-day > div"), U = I.eq(0).find(".fc-day-content > div"), B(_.add(_.find("tr"))), B(I), I.eq(0).addClass("fc-first"), I.filter(":last").addClass("fc-last"), X.each(function(e, n) {
                var r = Ee(Math.floor(e / ne), e % ne);
                ge("dayRender", O, r, t(n))
            }), y(X)
        }

        function c() {
            var t = "<table class='fc-border-separate' style='width:100%' cellspacing='0'>" + u() + v() + "</table>";
            return t
        }

        function u() {
            var t, e, n = le + "-widget-header",
                r = "";
            for (r += "<thead><tr>", ue && (r += "<th class='fc-week-number " + n + "'>" + q(de) + "</th>"), t = 0; ne > t; t++) e = Ee(0, t), r += "<th class='fc-day-header fc-" + ke[e.getDay()] + " " + n + "'>" + q(xe(e, ce)) + "</th>";
            return r += "</tr></thead>"
        }

        function v() {
            var t, e, n, r = le + "-widget-content",
                a = "";
            for (a += "<tbody>", t = 0; ee > t; t++) {
                for (a += "<tr class='fc-week'>", ue && (n = Ee(t, 0), a += "<td class='fc-week-number " + r + "'>" + "<div>" + q(xe(n, ve)) + "</div>" + "</td>"), e = 0; ne > e; e++) n = Ee(t, e), a += h(n);
                a += "</tr>"
            }
            return a += "</tbody>"
        }

        function h(t) {
            var e = le + "-widget-content",
                n = O.start.getMonth(),
                r = f(new Date),
                a = "",
                o = ["fc-day", "fc-" + ke[t.getDay()], e];
            return t.getMonth() != n && o.push("fc-other-month"), +t == +r ? o.push("fc-today", le + "-state-highlight") : r > t ? o.push("fc-past") : o.push("fc-future"), a += "<td class='" + o.join(" ") + "'" + " data-date='" + xe(t, "yyyy-MM-dd") + "'" + ">" + "<div>", re && (a += "<div class='fc-day-number'>" + t.getDate() + "</div>"), a += "<div class='fc-day-content'><div style='position:relative'>&nbsp;</div></div></div></td>"
        }

        function g(e) {
            Q = e;
            var n, r, a, o = Q - _.height();
            "variable" == he("weekMode") ? n = r = Math.floor(o / (1 == ee ? 2 : 6)) : (n = Math.floor(o / ee), r = o - n * (ee - 1)), J.each(function(e, o) {
                ee > e && (a = t(o), a.find("> div").css("min-height", (e == ee - 1 ? r : n) - R(a)))
            })
        }

        function p(t) {
            $ = t, ie.clear(), se.clear(), te = 0, ue && (te = _.find("th.fc-week-number").outerWidth()), K = Math.floor(($ - te) / ne), S(P.slice(0, -1), K)
        }

        function y(t) {
            t.click(w).mousedown(Me)
        }

        function w(e) {
            if (!he("selectable")) {
                var n = m(t(this).data("date"));
                ge("dayClick", this, n, !0, e)
            }
        }

        function b(t, e, n) {
            n && ae.build();
            for (var r = Te(t, e), a = 0; r.length > a; a++) {
                var o = r[a];
                y(D(o.row, o.leftCol, o.row, o.rightCol))
            }
        }

        function D(t, n, r, a) {
            var o = ae.rect(t, n, r, a, e);
            return be(o, e)
        }

        function C(t) {
            return d(t)
        }

        function M(t, e) {
            b(t, l(d(e), 1), !0)
        }

        function E() {
            Ce()
        }

        function T(t, e, n) {
            var r = Se(t),
                a = X[r.row * ne + r.col];
            ge("dayClick", a, t, e, n)
        }

        function x(t, e) {
            oe.start(function(t) {
                Ce(), t && D(t.row, t.col, t.row, t.col)
            }, e)
        }

        function k(t, e, n) {
            var r = oe.stop();
            if (Ce(), r) {
                var a = Ee(r);
                ge("drop", t, a, !0, e, n)
            }
        }

        function H(t) {
            return d(t.start)
        }

        function F(t) {
            return ie.left(t)
        }

        function N(t) {
            return ie.right(t)
        }

        function z(t) {
            return se.left(t)
        }

        function W(t) {
            return se.right(t)
        }

        function A(t) {
            return I.eq(t)
        }
        var O = this;
        O.renderBasic = a, O.setHeight = g, O.setWidth = p, O.renderDayOverlay = b, O.defaultSelectionEnd = C, O.renderSelection = M, O.clearSelection = E, O.reportDayClick = T, O.dragStart = x, O.dragStop = k, O.defaultEventEnd = H, O.getHoverListener = function() {
            return oe
        }, O.colLeft = F, O.colRight = N, O.colContentLeft = z, O.colContentRight = W, O.getIsCellAllDay = function() {
            return !0
        }, O.allDayRow = A, O.getRowCnt = function() {
            return ee
        }, O.getColCnt = function() {
            return ne
        }, O.getColWidth = function() {
            return K
        }, O.getDaySegmentContainer = function() {
            return Z
        }, fe.call(O, e, n, r), me.call(O), pe.call(O), G.call(O);
        var L, _, P, j, I, X, J, V, U, Z, $, Q, K, te, ee, ne, re, ae, oe, ie, se, le, ce, ue, de, ve, he = O.opt,
            ge = O.trigger,
            be = O.renderOverlay,
            Ce = O.clearOverlays,
            Me = O.daySelectionMousedown,
            Ee = O.cellToDate,
            Se = O.dateToCell,
            Te = O.rangeToSegments,
            xe = n.formatDate;
        Y(e.addClass("fc-grid")), ae = new ye(function(e, n) {
            var r, a, o;
            P.each(function(e, i) {
                r = t(i), a = r.offset().left, e && (o[1] = a), o = [a], n[e] = o
            }), o[1] = a + r.outerWidth(), I.each(function(n, i) {
                ee > n && (r = t(i), a = r.offset().top, n && (o[1] = a), o = [a], e[n] = o)
            }), o[1] = a + r.outerHeight()
        }), oe = new we(ae), ie = new De(function(t) {
            return V.eq(t)
        }), se = new De(function(t) {
            return U.eq(t)
        })
    }

    function G() {
        function t(t, e) {
            n.renderDayEvents(t, e)
        }

        function e() {
            n.getDaySegmentContainer().empty()
        }
        var n = this;
        n.renderEvents = t, n.clearEvents = e, de.call(n)
    }

    function $(t, e) {
        function n(t, e) {
            e && l(t, 7 * e);
            var n = l(d(t), -((t.getDay() - a("firstDay") + 7) % 7)),
                u = l(d(n), 7),
                f = d(n);
            i(f);
            var v = d(u);
            i(v, -1, !0);
            var h = s();
            r.title = c(f, l(d(v), -1), a("titleFormat")), r.start = n, r.end = u, r.visStart = f, r.visEnd = v, o(h)
        }
        var r = this;
        r.render = n, K.call(r, t, e, "agendaWeek");
        var a = r.opt,
            o = r.renderAgenda,
            i = r.skipHiddenDays,
            s = r.getCellsPerWeek,
            c = e.formatDates
    }

    function Q(t, e) {
        function n(t, e) {
            e && l(t, e), i(t, 0 > e ? -1 : 1);
            var n = d(t, !0),
                c = l(d(n), 1);
            r.title = s(t, a("titleFormat")), r.start = r.visStart = n, r.end = r.visEnd = c, o(1)
        }
        var r = this;
        r.render = n, K.call(r, t, e, "agendaDay");
        var a = r.opt,
            o = r.renderAgenda,
            i = r.skipHiddenDays,
            s = e.formatDate
    }

    function K(n, r, a) {
        function o(t) {
            We = t, i(), K ? c() : s()
        }

        function i() {
            qe = Ue("theme") ? "ui" : "fc", Ye = Ue("isRTL"), Be = y(Ue("minTime")), je = y(Ue("maxTime")), Ie = Ue("columnFormat"), Xe = Ue("weekNumbers"), Je = Ue("weekNumberTitle"), Ve = "iso" != Ue("weekNumberCalculation") ? "w" : "W", Re = Ue("snapMinutes") || Ue("slotMinutes")
        }

        function s() {
            var e, r, a, o, i, s = qe + "-widget-header",
                l = qe + "-widget-content",
                f = 0 == Ue("slotMinutes") % 15;
            for (c(), ce = t("<div style='position:absolute;z-index:2;left:0;width:100%'/>").appendTo(n), Ue("allDaySlot") ? (ue = t("<div class='fc-event-container' style='position:absolute;z-index:8;top:0;left:0'/>").appendTo(ce), e = "<table style='width:100%' class='fc-agenda-allday' cellspacing='0'><tr><th class='" + s + " fc-agenda-axis'>" + Ue("allDayText") + "</th>" + "<td>" + "<div class='fc-day-content'><div style='position:relative'/></div>" + "</td>" + "<th class='" + s + " fc-agenda-gutter'>&nbsp;</th>" + "</tr>" + "</table>", de = t(e).appendTo(ce), ve = de.find("tr"), C(ve.find("td")), ce.append("<div class='fc-agenda-divider " + s + "'>" + "<div class='fc-agenda-divider-inner'/>" + "</div>")) : ue = t([]), he = t("<div style='position:absolute;width:100%;overflow-x:hidden;overflow-y:auto'/>").appendTo(ce), ge = t("<div style='position:relative;width:100%;overflow:hidden'/>").appendTo(he), be = t("<div class='fc-event-container' style='position:absolute;z-index:8;top:0;left:0'/>").appendTo(ge), e = "<table class='fc-agenda-slots' style='width:100%' cellspacing='0'><tbody>", r = v(), o = u(d(r), je), u(r, Be), Ae = 0, a = 0; o > r; a++) i = r.getMinutes(), e += "<tr class='fc-slot" + a + " " + (i ? "fc-minor" : "") + "'>" + "<th class='fc-agenda-axis " + s + "'>" + (f && i ? "&nbsp;" : on(r, Ue("axisFormat"))) + "</th>" + "<td class='" + l + "'>" + "<div style='position:relative'>&nbsp;</div>" + "</td>" + "</tr>", u(r, Ue("slotMinutes")), Ae++;
            e += "</tbody></table>", Ce = t(e).appendTo(ge), M(Ce.find("td"))
        }

        function c() {
            var e = h();
            K && K.remove(), K = t(e).appendTo(n), ee = K.find("thead"), ne = ee.find("th").slice(1, -1), re = K.find("tbody"), ae = re.find("td").slice(0, -1), oe = ae.find("> div"), ie = ae.find(".fc-day-content > div"), se = ae.eq(0), le = oe.eq(0), B(ee.add(ee.find("tr"))), B(re.add(re.find("tr")))
        }

        function h() {
            var t = "<table style='width:100%' class='fc-agenda-days fc-border-separate' cellspacing='0'>" + g() + p() + "</table>";
            return t
        }

        function g() {
            var t, e, n, r = qe + "-widget-header",
                a = "";
            for (a += "<thead><tr>", Xe ? (t = nn(0, 0), e = on(t, Ve), Ye ? e += Je : e = Je + e, a += "<th class='fc-agenda-axis fc-week-number " + r + "'>" + q(e) + "</th>") : a += "<th class='fc-agenda-axis " + r + "'>&nbsp;</th>", n = 0; We > n; n++) t = nn(0, n), a += "<th class='fc-" + ke[t.getDay()] + " fc-col" + n + " " + r + "'>" + q(on(t, Ie)) + "</th>";
            return a += "<th class='fc-agenda-gutter " + r + "'>&nbsp;</th>" + "</tr>" + "</thead>"
        }

        function p() {
            var t, e, n, r, a, o = qe + "-widget-header",
                i = qe + "-widget-content",
                s = f(new Date),
                l = "";
            for (l += "<tbody><tr><th class='fc-agenda-axis " + o + "'>&nbsp;</th>", n = "", e = 0; We > e; e++) t = nn(0, e), a = ["fc-col" + e, "fc-" + ke[t.getDay()], i], +t == +s ? a.push(qe + "-state-highlight", "fc-today") : s > t ? a.push("fc-past") : a.push("fc-future"), r = "<td class='" + a.join(" ") + "'>" + "<div>" + "<div class='fc-day-content'>" + "<div style='position:relative'>&nbsp;</div>" + "</div>" + "</div>" + "</td>", n += r;
            return l += n, l += "<td class='fc-agenda-gutter " + i + "'>&nbsp;</td>" + "</tr>" + "</tbody>"
        }

        function m(t) {
            t === e && (t = Se), Se = t, sn = {};
            var n = re.position().top,
                r = he.position().top,
                a = Math.min(t - n, Ce.height() + r + 1);
            le.height(a - R(se)), ce.css("top", n), he.height(a - r - 1), Fe = Ce.find("tr:first").height() + 1, Ne = Ue("slotMinutes") / Re, ze = Fe / Ne
        }

        function w(e) {
            Ee = e, _e.clear(), Pe.clear();
            var n = ee.find("th:first");
            de && (n = n.add(de.find("th:first"))), n = n.add(Ce.find("th:first")), Te = 0, S(n.width("").each(function(e, n) {
                Te = Math.max(Te, t(n).outerWidth())
            }), Te);
            var r = K.find(".fc-agenda-gutter");
            de && (r = r.add(de.find("th.fc-agenda-gutter")));
            var a = he[0].clientWidth;
            He = he.width() - a, He ? (S(r, He), r.show().prev().removeClass("fc-last")) : r.hide().prev().addClass("fc-last"), xe = Math.floor((a - Te) / We), S(ne.slice(0, -1), xe)
        }

        function b() {
            function t() {
                he.scrollTop(r)
            }
            var e = v(),
                n = d(e);
            n.setHours(Ue("firstHour"));
            var r = _(e, n) + 1;
            t(), setTimeout(t, 0)
        }

        function D() {
            b()
        }

        function C(t) {
            t.click(E).mousedown(tn)
        }

        function M(t) {
            t.click(E).mousedown(U)
        }

        function E(t) {
            if (!Ue("selectable")) {
                var e = Math.min(We - 1, Math.floor((t.pageX - K.offset().left - Te) / xe)),
                    n = nn(0, e),
                    r = this.parentNode.className.match(/fc-slot(\d+)/);
                if (r) {
                    var a = parseInt(r[1]) * Ue("slotMinutes"),
                        o = Math.floor(a / 60);
                    n.setHours(o), n.setMinutes(a % 60 + Be), Ze("dayClick", ae[e], n, !1, t)
                } else Ze("dayClick", ae[e], n, !0, t)
            }
        }

        function x(t, e, n) {
            n && Oe.build();
            for (var r = an(t, e), a = 0; r.length > a; a++) {
                var o = r[a];
                C(k(o.row, o.leftCol, o.row, o.rightCol))
            }
        }

        function k(t, e, n, r) {
            var a = Oe.rect(t, e, n, r, ce);
            return Ge(a, ce)
        }

        function H(t, e) {
            for (var n = 0; We > n; n++) {
                var r = nn(0, n),
                    a = l(d(r), 1),
                    o = new Date(Math.max(r, t)),
                    i = new Date(Math.min(a, e));
                if (i > o) {
                    var s = Oe.rect(0, n, 0, n, ge),
                        c = _(r, o),
                        u = _(r, i);
                    s.top = c, s.height = u - c, M(Ge(s, ge))
                }
            }
        }

        function F(t) {
            return _e.left(t)
        }

        function N(t) {
            return Pe.left(t)
        }

        function z(t) {
            return _e.right(t)
        }

        function W(t) {
            return Pe.right(t)
        }

        function A(t) {
            return Ue("allDaySlot") && !t.row
        }

        function L(t) {
            var e = nn(0, t.col),
                n = t.row;
            return Ue("allDaySlot") && n--, n >= 0 && u(e, Be + n * Re), e
        }

        function _(t, n) {
            if (t = d(t, !0), u(d(t), Be) > n) return 0;
            if (n >= u(d(t), je)) return Ce.height();
            var r = Ue("slotMinutes"),
                a = 60 * n.getHours() + n.getMinutes() - Be,
                o = Math.floor(a / r),
                i = sn[o];
            return i === e && (i = sn[o] = Ce.find("tr").eq(o).find("td div")[0].offsetTop), Math.max(0, Math.round(i - 1 + Fe * (a % r / r)))
        }

        function P() {
            return ve
        }

        function j(t) {
            var e = d(t.start);
            return t.allDay ? e : u(e, Ue("defaultEventMinutes"))
        }

        function I(t, e) {
            return e ? d(t) : u(d(t), Ue("slotMinutes"))
        }

        function X(t, e, n) {
            n ? Ue("allDaySlot") && x(t, l(d(e), 1), !0) : J(t, e)
        }

        function J(e, n) {
            var r = Ue("selectHelper");
            if (Oe.build(), r) {
                var a = rn(e).col;
                if (a >= 0 && We > a) {
                    var o = Oe.rect(0, a, 0, a, ge),
                        i = _(e, e),
                        s = _(e, n);
                    if (s > i) {
                        if (o.top = i, o.height = s - i, o.left += 2, o.width -= 5, t.isFunction(r)) {
                            var l = r(e, n);
                            l && (o.position = "absolute", Me = t(l).css(o).appendTo(ge))
                        } else o.isStart = !0, o.isEnd = !0, Me = t(en({
                            title: "",
                            start: e,
                            end: n,
                            className: ["fc-select-helper"],
                            editable: !1
                        }, o)), Me.css("opacity", Ue("dragOpacity"));
                        Me && (M(Me), ge.append(Me), S(Me, o.width, !0), T(Me, o.height, !0))
                    }
                }
            } else H(e, n)
        }

        function V() {
            $e(), Me && (Me.remove(), Me = null)
        }

        function U(e) {
            if (1 == e.which && Ue("selectable")) {
                Ke(e);
                var n;
                Le.start(function(t, e) {
                    if (V(), t && t.col == e.col && !A(t)) {
                        var r = L(e),
                            a = L(t);
                        n = [r, u(d(r), Re), a, u(d(a), Re)].sort(O), J(n[0], n[3])
                    } else n = null
                }, e), t(document).one("mouseup", function(t) {
                    Le.stop(), n && (+n[0] == +n[1] && Z(n[0], !1, t), Qe(n[0], n[3], !1, t))
                })
            }
        }

        function Z(t, e, n) {
            Ze("dayClick", ae[rn(t).col], t, e, n)
        }

        function G(t, e) {
            Le.start(function(t) {
                if ($e(), t)
                    if (A(t)) k(t.row, t.col, t.row, t.col);
                    else {
                        var e = L(t),
                            n = u(d(e), Ue("defaultEventMinutes"));
                        H(e, n)
                    }
            }, e)
        }

        function $(t, e, n) {
            var r = Le.stop();
            $e(), r && Ze("drop", t, L(r), A(r), e, n)
        }
        var Q = this;
        Q.renderAgenda = o, Q.setWidth = w, Q.setHeight = m, Q.afterRender = D, Q.defaultEventEnd = j, Q.timePosition = _, Q.getIsCellAllDay = A, Q.allDayRow = P, Q.getCoordinateGrid = function() {
            return Oe
        }, Q.getHoverListener = function() {
            return Le
        }, Q.colLeft = F, Q.colRight = z, Q.colContentLeft = N, Q.colContentRight = W, Q.getDaySegmentContainer = function() {
            return ue
        }, Q.getSlotSegmentContainer = function() {
            return be
        }, Q.getMinMinute = function() {
            return Be
        }, Q.getMaxMinute = function() {
            return je
        }, Q.getSlotContainer = function() {
            return ge
        }, Q.getRowCnt = function() {
            return 1
        }, Q.getColCnt = function() {
            return We
        }, Q.getColWidth = function() {
            return xe
        }, Q.getSnapHeight = function() {
            return ze
        }, Q.getSnapMinutes = function() {
            return Re
        }, Q.defaultSelectionEnd = I, Q.renderDayOverlay = x, Q.renderSelection = X, Q.clearSelection = V, Q.reportDayClick = Z, Q.dragStart = G, Q.dragStop = $, fe.call(Q, n, r, a), me.call(Q), pe.call(Q), te.call(Q);
        var K, ee, ne, re, ae, oe, ie, se, le, ce, ue, de, ve, he, ge, be, Ce, Me, Ee, Se, Te, xe, He, Fe, Re, Ne, ze, We, Ae, Oe, Le, _e, Pe, qe, Ye, Be, je, Ie, Xe, Je, Ve, Ue = Q.opt,
            Ze = Q.trigger,
            Ge = Q.renderOverlay,
            $e = Q.clearOverlays,
            Qe = Q.reportSelection,
            Ke = Q.unselect,
            tn = Q.daySelectionMousedown,
            en = Q.slotSegHtml,
            nn = Q.cellToDate,
            rn = Q.dateToCell,
            an = Q.rangeToSegments,
            on = r.formatDate,
            sn = {};
        Y(n.addClass("fc-agenda")), Oe = new ye(function(e, n) {
            function r(t) {
                return Math.max(l, Math.min(c, t))
            }
            var a, o, i;
            ne.each(function(e, r) {
                a = t(r), o = a.offset().left, e && (i[1] = o), i = [o], n[e] = i
            }), i[1] = o + a.outerWidth(), Ue("allDaySlot") && (a = ve, o = a.offset().top, e[0] = [o, o + a.outerHeight()]);
            for (var s = ge.offset().top, l = he.offset().top, c = l + he.outerHeight(), u = 0; Ae * Ne > u; u++) e.push([r(s + ze * u), r(s + ze * (u + 1))])
        }), Le = new we(Oe), _e = new De(function(t) {
            return oe.eq(t)
        }), Pe = new De(function(t) {
            return ie.eq(t)
        })
    }

    function te() {
        function n(t, e) {
            var n, r = t.length,
                o = [],
                i = [];
            for (n = 0; r > n; n++) t[n].allDay ? o.push(t[n]) : i.push(t[n]);
            y("allDaySlot") && (te(o, e), k()), s(a(i), e)
        }

        function r() {
            H().empty(), F().empty()
        }

        function a(e) {
            var n, r, a, s, l, c = Y(),
                f = W(),
                v = z(),
                h = t.map(e, i),
                g = [];
            for (r = 0; c > r; r++)
                for (n = P(0, r), u(n, f), l = o(e, h, n, u(d(n), v - f)), l = ee(l), a = 0; l.length > a; a++) s = l[a], s.col = r, g.push(s);
            return g
        }

        function o(t, e, n, r) {
            var a, o, i, s, l, c, u, f, v = [],
                h = t.length;
            for (a = 0; h > a; a++) o = t[a], i = o.start, s = e[a], s > n && r > i && (n > i ? (l = d(n), u = !1) : (l = i, u = !0), s > r ? (c = d(r), f = !1) : (c = s, f = !0), v.push({
                event: o,
                start: l,
                end: c,
                isStart: u,
                isEnd: f
            }));
            return v.sort(ue)
        }

        function i(t) {
            return t.end ? d(t.end) : u(d(t.start), y("defaultEventMinutes"))
        }

        function s(n, r) {
            var a, o, i, s, l, u, d, v, h, g, p, m, b, D, C, M, S = n.length,
                T = "",
                k = F(),
                H = y("isRTL");
            for (a = 0; S > a; a++) o = n[a], i = o.event, s = A(o.start, o.start), l = A(o.start, o.end), u = L(o.col), d = _(o.col), v = d - u, d -= .025 * v, v = d - u, h = v * (o.forwardCoord - o.backwardCoord), y("slotEventOverlap") && (h = Math.max(2 * (h - 10), h)), H ? (p = d - o.backwardCoord * v, g = p - h) : (g = u + o.backwardCoord * v, p = g + h), g = Math.max(g, u), p = Math.min(p, d), h = p - g, o.top = s, o.left = g, o.outerWidth = h, o.outerHeight = l - s, T += c(i, o);
            for (k[0].innerHTML = T, m = k.children(), a = 0; S > a; a++) o = n[a], i = o.event, b = t(m[a]), D = w("eventRender", i, i, b), D === !1 ? b.remove() : (D && D !== !0 && (b.remove(), b = t(D).css({
                position: "absolute",
                top: o.top,
                left: o.left
            }).appendTo(k)), o.element = b, i._id === r ? f(i, b, o) : b[0]._fci = a, V(i, b));
            for (E(k, n, f), a = 0; S > a; a++) o = n[a], (b = o.element) && (o.vsides = R(b, !0), o.hsides = x(b, !0), C = b.find(".fc-event-title"), C.length && (o.contentTop = C[0].offsetTop));
            for (a = 0; S > a; a++) o = n[a], (b = o.element) && (b[0].style.width = Math.max(0, o.outerWidth - o.hsides) + "px", M = Math.max(0, o.outerHeight - o.vsides), b[0].style.height = M + "px", i = o.event, o.contentTop !== e && 10 > M - o.contentTop && (b.find("div.fc-event-time").text(re(i.start, y("timeFormat")) + " - " + i.title), b.find("div.fc-event-title").remove()), w("eventAfterRender", i, i, b))
        }

        function c(t, e) {
            var n = "<",
                r = t.url,
                a = j(t, y),
                o = ["fc-event", "fc-event-vert"];
            return b(t) && o.push("fc-event-draggable"), e.isStart && o.push("fc-event-start"), e.isEnd && o.push("fc-event-end"), o = o.concat(t.className), t.source && (o = o.concat(t.source.className || [])), n += r ? "a href='" + q(t.url) + "'" : "div", n += " class='" + o.join(" ") + "'" + " style=" + "'" + "position:absolute;" + "top:" + e.top + "px;" + "left:" + e.left + "px;" + a + "'" + ">" + "<div class='fc-event-inner'>" + "<div class='fc-event-time'>" + q(ae(t.start, t.end, y("timeFormat"))) + "</div>" + "<div class='fc-event-title'>" + q(t.title || "") + "</div>" + "</div>" + "<div class='fc-event-bg'></div>", e.isEnd && D(t) && (n += "<div class='ui-resizable-handle ui-resizable-s'>=</div>"), n += "</" + (r ? "a" : "div") + ">"
        }

        function f(t, e, n) {
            var r = e.find("div.fc-event-time");
            b(t) && g(t, e, r), n.isEnd && D(t) && p(t, e, r), S(t, e)
        }

        function v(t, e, n) {
            function r() {
                c || (e.width(a).height("").draggable("option", "grid", null), c = !0)
            }
            var a, o, i, s = n.isStart,
                c = !0,
                u = N(),
                f = B(),
                v = I(),
                g = X(),
                p = W();
            e.draggable({
                opacity: y("dragOpacity", "month"),
                revertDuration: y("dragRevertDuration"),
                start: function(n, p) {
                    w("eventDragStart", e, t, n, p), Z(t, e), a = e.width(), u.start(function(n, a) {
                        if (K(), n) {
                            o = !1;
                            var u = P(0, a.col),
                                p = P(0, n.col);
                            i = h(p, u), n.row ? s ? c && (e.width(f - 10), T(e, v * Math.round((t.end ? (t.end - t.start) / Re : y("defaultEventMinutes")) / g)), e.draggable("option", "grid", [f, 1]), c = !1) : o = !0 : (Q(l(d(t.start), i), l(C(t), i)), r()), o = o || c && !i
                        } else r(), o = !0;
                        e.draggable("option", "revert", o)
                    }, n, "drag")
                },
                stop: function(n, a) {
                    if (u.stop(), K(), w("eventDragStop", e, t, n, a), o) r(), e.css("filter", ""), U(t, e);
                    else {
                        var s = 0;
                        c || (s = Math.round((e.offset().top - J().offset().top) / v) * g + p - (60 * t.start.getHours() + t.start.getMinutes())), G(this, t, i, s, c, n, a)
                    }
                }
            })
        }

        function g(t, e, n) {
            function r() {
                K(), s && (f ? (n.hide(), e.draggable("option", "grid", null), Q(l(d(t.start), b), l(C(t), b))) : (a(D), n.css("display", ""), e.draggable("option", "grid", [T, x])))
            }

            function a(e) {
                var r, a = u(d(t.start), e);
                t.end && (r = u(d(t.end), e)), n.text(ae(a, r, y("timeFormat")))
            }
            var o, i, s, c, f, v, g, p, b, D, M, E = m.getCoordinateGrid(),
                S = Y(),
                T = B(),
                x = I(),
                k = X();
            e.draggable({
                scroll: !1,
                grid: [T, x],
                axis: 1 == S ? "y" : !1,
                opacity: y("dragOpacity"),
                revertDuration: y("dragRevertDuration"),
                start: function(n, r) {
                    w("eventDragStart", e, t, n, r), Z(t, e), E.build(), o = e.position(), i = E.cell(n.pageX, n.pageY), s = c = !0, f = v = O(i), g = p = 0, b = 0, D = M = 0
                },
                drag: function(t, n) {
                    var a = E.cell(t.pageX, t.pageY);
                    if (s = !!a) {
                        if (f = O(a), g = Math.round((n.position.left - o.left) / T), g != p) {
                            var l = P(0, i.col),
                                u = i.col + g;
                            u = Math.max(0, u), u = Math.min(S - 1, u);
                            var d = P(0, u);
                            b = h(d, l)
                        }
                        f || (D = Math.round((n.position.top - o.top) / x) * k)
                    }(s != c || f != v || g != p || D != M) && (r(), c = s, v = f, p = g, M = D), e.draggable("option", "revert", !s)
                },
                stop: function(n, a) {
                    K(), w("eventDragStop", e, t, n, a), s && (f || b || D) ? G(this, t, b, f ? 0 : D, f, n, a) : (s = !0, f = !1, g = 0, b = 0, D = 0, r(), e.css("filter", ""), e.css(o), U(t, e))
                }
            })
        }

        function p(t, e, n) {
            var r, a, o = I(),
                i = X();
            e.resizable({
                handles: {
                    s: ".ui-resizable-handle"
                },
                grid: o,
                start: function(n, o) {
                    r = a = 0, Z(t, e), w("eventResizeStart", this, t, n, o)
                },
                resize: function(s, l) {
                    r = Math.round((Math.max(o, e.height()) - l.originalSize.height) / o), r != a && (n.text(ae(t.start, r || t.end ? u(M(t), i * r) : null, y("timeFormat"))), a = r)
                },
                stop: function(n, a) {
                    w("eventResizeStop", this, t, n, a), r ? $(this, t, 0, i * r, n, a) : U(t, e)
                }
            })
        }
        var m = this;
        m.renderEvents = n, m.clearEvents = r, m.slotSegHtml = c, de.call(m);
        var y = m.opt,
            w = m.trigger,
            b = m.isEventDraggable,
            D = m.isEventResizable,
            M = m.eventEnd,
            S = m.eventElementHandlers,
            k = m.setHeight,
            H = m.getDaySegmentContainer,
            F = m.getSlotSegmentContainer,
            N = m.getHoverListener,
            z = m.getMaxMinute,
            W = m.getMinMinute,
            A = m.timePosition,
            O = m.getIsCellAllDay,
            L = m.colContentLeft,
            _ = m.colContentRight,
            P = m.cellToDate,
            Y = m.getColCnt,
            B = m.getColWidth,
            I = m.getSnapHeight,
            X = m.getSnapMinutes,
            J = m.getSlotContainer,
            V = m.reportEventElement,
            U = m.showEvents,
            Z = m.hideEvents,
            G = m.eventDrop,
            $ = m.eventResize,
            Q = m.renderDayOverlay,
            K = m.clearOverlays,
            te = m.renderDayEvents,
            ne = m.calendar,
            re = ne.formatDate,
            ae = ne.formatDates;
        m.draggableDayEvent = v
    }

    function ee(t) {
        var e, n = ne(t),
            r = n[0];
        if (re(n), r) {
            for (e = 0; r.length > e; e++) ae(r[e]);
            for (e = 0; r.length > e; e++) oe(r[e], 0, 0)
        }
        return ie(n)
    }

    function ne(t) {
        var e, n, r, a = [];
        for (e = 0; t.length > e; e++) {
            for (n = t[e], r = 0; a.length > r && se(n, a[r]).length; r++);
            (a[r] || (a[r] = [])).push(n)
        }
        return a
    }

    function re(t) {
        var e, n, r, a, o;
        for (e = 0; t.length > e; e++)
            for (n = t[e], r = 0; n.length > r; r++)
                for (a = n[r], a.forwardSegs = [], o = e + 1; t.length > o; o++) se(a, t[o], a.forwardSegs)
    }

    function ae(t) {
        var n, r, a = t.forwardSegs,
            o = 0;
        if (t.forwardPressure === e) {
            for (n = 0; a.length > n; n++) r = a[n], ae(r), o = Math.max(o, 1 + r.forwardPressure);
            t.forwardPressure = o
        }
    }

    function oe(t, n, r) {
        var a, o = t.forwardSegs;
        if (t.forwardCoord === e)
            for (o.length ? (o.sort(ce), oe(o[0], n + 1, r), t.forwardCoord = o[0].backwardCoord) : t.forwardCoord = 1, t.backwardCoord = t.forwardCoord - (t.forwardCoord - r) / (n + 1), a = 0; o.length > a; a++) oe(o[a], 0, t.forwardCoord)
    }

    function ie(t) {
        var e, n, r, a = [];
        for (e = 0; t.length > e; e++)
            for (n = t[e], r = 0; n.length > r; r++) a.push(n[r]);
        return a
    }

    function se(t, e, n) {
        n = n || [];
        for (var r = 0; e.length > r; r++) le(t, e[r]) && n.push(e[r]);
        return n
    }

    function le(t, e) {
        return t.end > e.start && t.start < e.end
    }

    function ce(t, e) {
        return e.forwardPressure - t.forwardPressure || (t.backwardCoord || 0) - (e.backwardCoord || 0) || ue(t, e)
    }

    function ue(t, e) {
        return t.start - e.start || e.end - e.start - (t.end - t.start) || (t.event.title || "").localeCompare(e.event.title)
    }

    function fe(n, r, a) {
        function o(e, n) {
            var r = V[e];
            return t.isPlainObject(r) ? P(r, n || a) : r
        }

        function i(t, e) {
            return r.trigger.apply(r, [t, e || _].concat(Array.prototype.slice.call(arguments, 2), [_]))
        }

        function s(t) {
            var e = t.source || {};
            return X(t.startEditable, e.startEditable, o("eventStartEditable"), t.editable, e.editable, o("editable")) && !o("disableDragging")
        }

        function c(t) {
            var e = t.source || {};
            return X(t.durationEditable, e.durationEditable, o("eventDurationEditable"), t.editable, e.editable, o("editable")) && !o("disableResizing")
        }

        function f(t) {
            j = {};
            var e, n, r = t.length;
            for (e = 0; r > e; e++) n = t[e], j[n._id] ? j[n._id].push(n) : j[n._id] = [n]
        }

        function v() {
            j = {}, I = {}, J = []
        }

        function g(t) {
            return t.end ? d(t.end) : q(t)
        }

        function p(t, e) {
            J.push({
                event: t,
                element: e
            }), I[t._id] ? I[t._id].push(e) : I[t._id] = [e]
        }

        function m() {
            t.each(J, function(t, e) {
                _.trigger("eventDestroy", e.event, e.event, e.element)
            })
        }

        function y(t, n) {
            n.click(function(r) {
                return n.hasClass("ui-draggable-dragging") || n.hasClass("ui-resizable-resizing") ? e : i("eventClick", this, t, r)
            }).hover(function(e) {
                i("eventMouseover", this, t, e)
            }, function(e) {
                i("eventMouseout", this, t, e)
            })
        }

        function w(t, e) {
            D(t, e, "show")
        }

        function b(t, e) {
            D(t, e, "hide")
        }

        function D(t, e, n) {
            var r, a = I[t._id],
                o = a.length;
            for (r = 0; o > r; r++) e && a[r][0] == e[0] || a[r][n]()
        }

        function C(t, e, n, r, a, o, s) {
            var l = e.allDay,
                c = e._id;
            E(j[c], n, r, a), i("eventDrop", t, e, n, r, a, function() {
                E(j[c], -n, -r, l), B(c)
            }, o, s), B(c)
        }

        function M(t, e, n, r, a, o) {
            var s = e._id;
            S(j[s], n, r), i("eventResize", t, e, n, r, function() {
                S(j[s], -n, -r), B(s)
            }, a, o), B(s)
        }

        function E(t, n, r, a) {
            r = r || 0;
            for (var o, i = t.length, s = 0; i > s; s++) o = t[s], a !== e && (o.allDay = a), u(l(o.start, n, !0), r), o.end && (o.end = u(l(o.end, n, !0), r)), Y(o, V)
        }

        function S(t, e, n) {
            n = n || 0;
            for (var r, a = t.length, o = 0; a > o; o++) r = t[o], r.end = u(l(g(r), e, !0), n), Y(r, V)
        }

        function T(t) {
            return "object" == typeof t && (t = t.getDay()), G[t]
        }

        function x() {
            return U
        }

        function k(t, e, n) {
            for (e = e || 1; G[(t.getDay() + (n ? e : 0) + 7) % 7];) l(t, e)
        }

        function H() {
            var t = F.apply(null, arguments),
                e = R(t),
                n = N(e);
            return n
        }

        function F(t, e) {
            var n = _.getColCnt(),
                r = K ? -1 : 1,
                a = K ? n - 1 : 0;
            "object" == typeof t && (e = t.col, t = t.row);
            var o = t * n + (e * r + a);
            return o
        }

        function R(t) {
            var e = _.visStart.getDay();
            return t += $[e], 7 * Math.floor(t / U) + Q[(t % U + U) % U] - e
        }

        function N(t) {
            var e = d(_.visStart);
            return l(e, t), e
        }

        function z(t) {
            var e = W(t),
                n = A(e),
                r = O(n);
            return r
        }

        function W(t) {
            return h(t, _.visStart)
        }

        function A(t) {
            var e = _.visStart.getDay();
            return t += e, Math.floor(t / 7) * U + $[(t % 7 + 7) % 7] - $[e]
        }

        function O(t) {
            var e = _.getColCnt(),
                n = K ? -1 : 1,
                r = K ? e - 1 : 0,
                a = Math.floor(t / e),
                o = (t % e + e) % e * n + r;
            return {
                row: a,
                col: o
            }
        }

        function L(t, e) {
            for (var n = _.getRowCnt(), r = _.getColCnt(), a = [], o = W(t), i = W(e), s = A(o), l = A(i) - 1, c = 0; n > c; c++) {
                var u = c * r,
                    f = u + r - 1,
                    d = Math.max(s, u),
                    v = Math.min(l, f);
                if (v >= d) {
                    var h = O(d),
                        g = O(v),
                        p = [h.col, g.col].sort(),
                        m = R(d) == o,
                        y = R(v) + 1 == i;
                    a.push({
                        row: c,
                        leftCol: p[0],
                        rightCol: p[1],
                        isStart: m,
                        isEnd: y
                    })
                }
            }
            return a
        }
        var _ = this;
        _.element = n, _.calendar = r, _.name = a, _.opt = o, _.trigger = i, _.isEventDraggable = s, _.isEventResizable = c, _.setEventData = f, _.clearEventData = v, _.eventEnd = g, _.reportEventElement = p, _.triggerEventDestroy = m, _.eventElementHandlers = y, _.showEvents = w, _.hideEvents = b, _.eventDrop = C, _.eventResize = M;
        var q = _.defaultEventEnd,
            Y = r.normalizeEvent,
            B = r.reportEventChange,
            j = {},
            I = {},
            J = [],
            V = r.options;
        _.isHiddenDay = T, _.skipHiddenDays = k, _.getCellsPerWeek = x, _.dateToCell = z, _.dateToDayOffset = W, _.dayOffsetToCellOffset = A, _.cellOffsetToCell = O, _.cellToDate = H, _.cellToCellOffset = F, _.cellOffsetToDayOffset = R, _.dayOffsetToDate = N, _.rangeToSegments = L;
        var U, Z = o("hiddenDays") || [],
            G = [],
            $ = [],
            Q = [],
            K = o("isRTL");
        (function() {
            o("weekends") === !1 && Z.push(0, 6);
            for (var e = 0, n = 0; 7 > e; e++) $[e] = n, G[e] = -1 != t.inArray(e, Z), G[e] || (Q[n] = e, n++);
            if (U = n, !U) throw "invalid hiddenDays"
        })()
    }

    function de() {
        function e(t, e) {
            var n = r(t, !1, !0);
            he(n, function(t, e) {
                N(t.event, e)
            }), w(n, e), he(n, function(t, e) {
                k("eventAfterRender", t.event, t.event, e)
            })
        }

        function n(t, e, n) {
            var a = r([t], !0, !1),
                o = [];
            return he(a, function(t, r) {
                t.row === e && r.css("top", n), o.push(r[0])
            }), o
        }

        function r(e, n, r) {
            var o, l, c = Z(),
                d = n ? t("<div/>") : c,
                v = a(e);
            return i(v), o = s(v), d[0].innerHTML = o, l = d.children(), n && c.append(l), u(v, l), he(v, function(t, e) {
                t.hsides = x(e, !0)
            }), he(v, function(t, e) {
                e.width(Math.max(0, t.outerWidth - t.hsides))
            }), he(v, function(t, e) {
                t.outerHeight = e.outerHeight(!0)
            }), f(v, r), v
        }

        function a(t) {
            for (var e = [], n = 0; t.length > n; n++) {
                var r = o(t[n]);
                e.push.apply(e, r)
            }
            return e
        }

        function o(t) {
            for (var e = t.start, n = C(t), r = ee(e, n), a = 0; r.length > a; a++) r[a].event = t;
            return r
        }

        function i(t) {
            for (var e = T("isRTL"), n = 0; t.length > n; n++) {
                var r = t[n],
                    a = (e ? r.isEnd : r.isStart) ? V : X,
                    o = (e ? r.isStart : r.isEnd) ? U : J,
                    i = a(r.leftCol),
                    s = o(r.rightCol);
                r.left = i, r.outerWidth = s - i
            }
        }

        function s(t) {
            for (var e = "", n = 0; t.length > n; n++) e += c(t[n]);
            return e
        }

        function c(t) {
            var e = "",
                n = T("isRTL"),
                r = t.event,
                a = r.url,
                o = ["fc-event", "fc-event-hori"];
            H(r) && o.push("fc-event-draggable"), t.isStart && o.push("fc-event-start"), t.isEnd && o.push("fc-event-end"), o = o.concat(r.className), r.source && (o = o.concat(r.source.className || []));
            var i = j(r, T);
            return e += a ? "<a href='" + q(a) + "'" : "<div", e += " class='" + o.join(" ") + "'" + " style=" + "'" + "position:absolute;" + "left:" + t.left + "px;" + i + "'" + ">" + "<div class='fc-event-inner'>", !r.allDay && t.isStart && (e += "<span class='fc-event-time'>" + q(G(r.start, r.end, T("timeFormat"))) + "</span>"), e += "<span class='fc-event-title'>" + q(r.title || "") + "</span>" + "</div>", t.isEnd && F(r) && (e += "<div class='ui-resizable-handle ui-resizable-" + (n ? "w" : "e") + "'>" + "&nbsp;&nbsp;&nbsp;" + "</div>"), e += "</" + (a ? "a" : "div") + ">"
        }

        function u(e, n) {
            for (var r = 0; e.length > r; r++) {
                var a = e[r],
                    o = a.event,
                    i = n.eq(r),
                    s = k("eventRender", o, o, i);
                s === !1 ? i.remove() : (s && s !== !0 && (s = t(s).css({
                    position: "absolute",
                    left: a.left
                }), i.replaceWith(s), i = s), a.element = i)
            }
        }

        function f(t, e) {
            var n = v(t),
                r = y(),
                a = [];
            if (e)
                for (var o = 0; r.length > o; o++) r[o].height(n[o]);
            for (var o = 0; r.length > o; o++) a.push(r[o].position().top);
            he(t, function(t, e) {
                e.css("top", a[t.row] + t.top)
            })
        }

        function v(t) {
            for (var e = P(), n = B(), r = [], a = g(t), o = 0; e > o; o++) {
                for (var i = a[o], s = [], l = 0; n > l; l++) s.push(0);
                for (var c = 0; i.length > c; c++) {
                    var u = i[c];
                    u.top = L(s.slice(u.leftCol, u.rightCol + 1));
                    for (var l = u.leftCol; u.rightCol >= l; l++) s[l] = u.top + u.outerHeight
                }
                r.push(L(s))
            }
            return r
        }

        function g(t) {
            var e, n, r, a = P(),
                o = [];
            for (e = 0; t.length > e; e++) n = t[e], r = n.row, n.element && (o[r] ? o[r].push(n) : o[r] = [n]);
            for (r = 0; a > r; r++) o[r] = p(o[r] || []);
            return o
        }

        function p(t) {
            for (var e = [], n = m(t), r = 0; n.length > r; r++) e.push.apply(e, n[r]);
            return e
        }

        function m(t) {
            t.sort(ge);
            for (var e = [], n = 0; t.length > n; n++) {
                for (var r = t[n], a = 0; e.length > a && ve(r, e[a]); a++);
                e[a] ? e[a].push(r) : e[a] = [r]
            }
            return e
        }

        function y() {
            var t, e = P(),
                n = [];
            for (t = 0; e > t; t++) n[t] = I(t).find("div.fc-day-content > div");
            return n
        }

        function w(t, e) {
            var n = Z();
            he(t, function(t, n, r) {
                var a = t.event;
                a._id === e ? b(a, n, t) : n[0]._fci = r
            }), E(n, t, b)
        }

        function b(t, e, n) {
            H(t) && S.draggableDayEvent(t, e, n), n.isEnd && F(t) && S.resizableDayEvent(t, e, n), z(t, e)
        }

        function D(t, e) {
            var n, r = te();
            e.draggable({
                delay: 50,
                opacity: T("dragOpacity"),
                revertDuration: T("dragRevertDuration"),
                start: function(a, o) {
                    k("eventDragStart", e, t, a, o), A(t, e), r.start(function(r, a, o, i) {
                        if (e.draggable("option", "revert", !r || !o && !i), Q(), r) {
                            var s = ne(a),
                                c = ne(r);
                            n = h(c, s), $(l(d(t.start), n), l(C(t), n))
                        } else n = 0
                    }, a, "drag")
                },
                stop: function(a, o) {
                    r.stop(), Q(), k("eventDragStop", e, t, a, o), n ? O(this, t, n, 0, t.allDay, a, o) : (e.css("filter", ""), W(t, e))
                }
            })
        }

        function M(e, r, a) {
            var o = T("isRTL"),
                i = o ? "w" : "e",
                s = r.find(".ui-resizable-" + i),
                c = !1;
            Y(r), r.mousedown(function(t) {
                t.preventDefault()
            }).click(function(t) {
                c && (t.preventDefault(), t.stopImmediatePropagation())
            }), s.mousedown(function(o) {
                function s(n) {
                    k("eventResizeStop", this, e, n), t("body").css("cursor", ""), u.stop(), Q(), f && _(this, e, f, 0, n), setTimeout(function() {
                        c = !1
                    }, 0)
                }
                if (1 == o.which) {
                    c = !0;
                    var u = te();
                    P(), B();
                    var f, d, v = r.css("top"),
                        h = t.extend({}, e),
                        g = ie(oe(e.start));
                    K(), t("body").css("cursor", i + "-resize").one("mouseup", s), k("eventResizeStart", this, e, o), u.start(function(r, o) {
                        if (r) {
                            var s = re(o),
                                c = re(r);
                            if (c = Math.max(c, g), f = ae(c) - ae(s)) {
                                h.end = l(R(e), f, !0);
                                var u = d;
                                d = n(h, a.row, v), d = t(d), d.find("*").css("cursor", i + "-resize"), u && u.remove(), A(e)
                            } else d && (W(e), d.remove(), d = null);
                            Q(), $(e.start, l(C(e), f))
                        }
                    }, o)
                }
            })
        }
        var S = this;
        S.renderDayEvents = e, S.draggableDayEvent = D, S.resizableDayEvent = M;
        var T = S.opt,
            k = S.trigger,
            H = S.isEventDraggable,
            F = S.isEventResizable,
            R = S.eventEnd,
            N = S.reportEventElement,
            z = S.eventElementHandlers,
            W = S.showEvents,
            A = S.hideEvents,
            O = S.eventDrop,
            _ = S.eventResize,
            P = S.getRowCnt,
            B = S.getColCnt;
        S.getColWidth;
        var I = S.allDayRow,
            X = S.colLeft,
            J = S.colRight,
            V = S.colContentLeft,
            U = S.colContentRight;
        S.dateToCell;
        var Z = S.getDaySegmentContainer,
            G = S.calendar.formatDates,
            $ = S.renderDayOverlay,
            Q = S.clearOverlays,
            K = S.clearSelection,
            te = S.getHoverListener,
            ee = S.rangeToSegments,
            ne = S.cellToDate,
            re = S.cellToCellOffset,
            ae = S.cellOffsetToDayOffset,
            oe = S.dateToDayOffset,
            ie = S.dayOffsetToCellOffset
    }

    function ve(t, e) {
        for (var n = 0; e.length > n; n++) {
            var r = e[n];
            if (r.leftCol <= t.rightCol && r.rightCol >= t.leftCol) return !0
        }
        return !1
    }

    function he(t, e) {
        for (var n = 0; t.length > n; n++) {
            var r = t[n],
                a = r.element;
            a && e(r, a, n)
        }
    }

    function ge(t, e) {
        return e.rightCol - e.leftCol - (t.rightCol - t.leftCol) || e.event.allDay - t.event.allDay || t.event.start - e.event.start || (t.event.title || "").localeCompare(e.event.title)
    }

    function pe() {
        function e(t, e, a) {
            n(), e || (e = l(t, a)), c(t, e, a), r(t, e, a)
        }

        function n(t) {
            f && (f = !1, u(), s("unselect", null, t))
        }

        function r(t, e, n, r) {
            f = !0, s("select", null, t, e, n, r)
        }

        function a(e) {
            var a = o.cellToDate,
                s = o.getIsCellAllDay,
                l = o.getHoverListener(),
                f = o.reportDayClick;
            if (1 == e.which && i("selectable")) {
                n(e);
                var d;
                l.start(function(t, e) {
                    u(), t && s(t) ? (d = [a(e), a(t)].sort(O), c(d[0], d[1], !0)) : d = null
                }, e), t(document).one("mouseup", function(t) {
                    l.stop(), d && (+d[0] == +d[1] && f(d[0], !0, t), r(d[0], d[1], !0, t))
                })
            }
        }
        var o = this;
        o.select = e, o.unselect = n, o.reportSelection = r, o.daySelectionMousedown = a;
        var i = o.opt,
            s = o.trigger,
            l = o.defaultSelectionEnd,
            c = o.renderSelection,
            u = o.clearSelection,
            f = !1;
        i("selectable") && i("unselectAuto") && t(document).mousedown(function(e) {
            var r = i("unselectCancel");
            r && t(e.target).parents(r).length || n(e)
        })
    }

    function me() {
        function e(e, n) {
            var r = o.shift();
            return r || (r = t("<div class='fc-cell-overlay' style='position:absolute;z-index:3'/>")), r[0].parentNode != n[0] && r.appendTo(n), a.push(r.css(e).show()), r
        }

        function n() {
            for (var t; t = a.shift();) o.push(t.hide().unbind())
        }
        var r = this;
        r.renderOverlay = e, r.clearOverlays = n;
        var a = [],
            o = []
    }

    function ye(t) {
        var e, n, r = this;
        r.build = function() {
            e = [], n = [], t(e, n)
        }, r.cell = function(t, r) {
            var a, o = e.length,
                i = n.length,
                s = -1,
                l = -1;
            for (a = 0; o > a; a++)
                if (r >= e[a][0] && e[a][1] > r) {
                    s = a;
                    break
                }
            for (a = 0; i > a; a++)
                if (t >= n[a][0] && n[a][1] > t) {
                    l = a;
                    break
                }
            return s >= 0 && l >= 0 ? {
                row: s,
                col: l
            } : null
        }, r.rect = function(t, r, a, o, i) {
            var s = i.offset();
            return {
                top: e[t][0] - s.top,
                left: n[r][0] - s.left,
                width: n[o][1] - n[r][0],
                height: e[a][1] - e[t][0]
            }
        }
    }

    function we(e) {
        function n(t) {
            be(t);
            var n = e.cell(t.pageX, t.pageY);
            (!n != !i || n && (n.row != i.row || n.col != i.col)) && (n ? (o || (o = n), a(n, o, n.row - o.row, n.col - o.col)) : a(n, o), i = n)
        }
        var r, a, o, i, s = this;
        s.start = function(s, l, c) {
            a = s, o = i = null, e.build(), n(l), r = c || "mousemove", t(document).bind(r, n)
        }, s.stop = function() {
            return t(document).unbind(r, n), i
        }
    }

    function be(t) {
        t.pageX === e && (t.pageX = t.originalEvent.pageX, t.pageY = t.originalEvent.pageY)
    }

    function De(t) {
        function n(e) {
            return a[e] = a[e] || t(e)
        }
        var r = this,
            a = {},
            o = {},
            i = {};
        r.left = function(t) {
            return o[t] = o[t] === e ? n(t).position().left : o[t]
        }, r.right = function(t) {
            return i[t] = i[t] === e ? r.left(t) + n(t).width() : i[t]
        }, r.clear = function() {
            a = {}, o = {}, i = {}
        }
    }
    var Ce = {
            defaultView: "month",
            aspectRatio: 1.35,
            header: {
                left: "title",
                center: "",
                right: "today prev,next"
            },
            weekends: !0,
            weekNumbers: !1,
            weekNumberCalculation: "iso",
            weekNumberTitle: "W",
            allDayDefault: !0,
            ignoreTimezone: !0,
            lazyFetching: !0,
            startParam: "start",
            endParam: "end",
            titleFormat: {
                month: "MMMM yyyy",
                week: "MMM d[ yyyy]{ '&#8212;'[ MMM] d yyyy}",
                day: "dddd, MMM d, yyyy"
            },
            columnFormat: {
                month: "ddd",
                week: "ddd M/d",
                day: "dddd M/d"
            },
            timeFormat: {
                "": "h(:mm)t"
            },
            isRTL: !1,
            firstDay: 0,
            monthNames: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            dayNames: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            dayNamesShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
            buttonText: {
                prev: "<span class='fc-text-arrow'>&lsaquo;</span>",
                next: "<span class='fc-text-arrow'>&rsaquo;</span>",
                prevYear: "<span class='fc-text-arrow'>&laquo;</span>",
                nextYear: "<span class='fc-text-arrow'>&raquo;</span>",
                today: "today",
                month: "month",
                week: "week",
                day: "day"
            },
            theme: !1,
            buttonIcons: {
                prev: "circle-triangle-w",
                next: "circle-triangle-e"
            },
            unselectAuto: !0,
            dropAccept: "*",
            handleWindowResize: !0
        },
        Me = {
            header: {
                left: "next,prev today",
                center: "",
                right: "title"
            },
            buttonText: {
                prev: "<span class='fc-text-arrow'>&rsaquo;</span>",
                next: "<span class='fc-text-arrow'>&lsaquo;</span>",
                prevYear: "<span class='fc-text-arrow'>&raquo;</span>",
                nextYear: "<span class='fc-text-arrow'>&laquo;</span>"
            },
            buttonIcons: {
                prev: "circle-triangle-e",
                next: "circle-triangle-w"
            }
        },
        Ee = t.fullCalendar = {
            version: "1.6.4"
        },
        Se = Ee.views = {};
    t.fn.fullCalendar = function(n) {
        if ("string" == typeof n) {
            var a, o = Array.prototype.slice.call(arguments, 1);
            return this.each(function() {
                var r = t.data(this, "fullCalendar");
                if (r && t.isFunction(r[n])) {
                    var i = r[n].apply(r, o);
                    a === e && (a = i), "destroy" == n && t.removeData(this, "fullCalendar")
                }
            }), a !== e ? a : this
        }
        n = n || {};
        var i = n.eventSources || [];
        return delete n.eventSources, n.events && (i.push(n.events), delete n.events), n = t.extend(!0, {}, Ce, n.isRTL || n.isRTL === e && Ce.isRTL ? Me : {}, n), this.each(function(e, a) {
            var o = t(a),
                s = new r(o, n, i);
            o.data("fullCalendar", s), s.render()
        }), this
    }, Ee.sourceNormalizers = [], Ee.sourceFetchers = [];
    var Te = {
            dataType: "json",
            cache: !1
        },
        xe = 1;
    Ee.addDays = l, Ee.cloneDate = d, Ee.parseDate = p, Ee.parseISO8601 = m, Ee.parseTime = y, Ee.formatDate = w, Ee.formatDates = b;
    var ke = ["sun", "mon", "tue", "wed", "thu", "fri", "sat"],
        He = 864e5,
        Fe = 36e5,
        Re = 6e4,
        Ne = {
            s: function(t) {
                return t.getSeconds()
            },
            ss: function(t) {
                return _(t.getSeconds())
            },
            m: function(t) {
                return t.getMinutes()
            },
            mm: function(t) {
                return _(t.getMinutes())
            },
            h: function(t) {
                return t.getHours() % 12 || 12
            },
            hh: function(t) {
                return _(t.getHours() % 12 || 12)
            },
            H: function(t) {
                return t.getHours()
            },
            HH: function(t) {
                return _(t.getHours())
            },
            d: function(t) {
                return t.getDate()
            },
            dd: function(t) {
                return _(t.getDate())
            },
            ddd: function(t, e) {
                return e.dayNamesShort[t.getDay()]
            },
            dddd: function(t, e) {
                return e.dayNames[t.getDay()]
            },
            M: function(t) {
                return t.getMonth() + 1
            },
            MM: function(t) {
                return _(t.getMonth() + 1)
            },
            MMM: function(t, e) {
                return e.monthNamesShort[t.getMonth()]
            },
            MMMM: function(t, e) {
                return e.monthNames[t.getMonth()]
            },
            yy: function(t) {
                return (t.getFullYear() + "").substring(2)
            },
            yyyy: function(t) {
                return t.getFullYear()
            },
            t: function(t) {
                return 12 > t.getHours() ? "a" : "p"
            },
            tt: function(t) {
                return 12 > t.getHours() ? "am" : "pm"
            },
            T: function(t) {
                return 12 > t.getHours() ? "A" : "P"
            },
            TT: function(t) {
                return 12 > t.getHours() ? "AM" : "PM"
            },
            u: function(t) {
                return w(t, "yyyy-MM-dd'T'HH:mm:ss'Z'")
            },
            S: function(t) {
                var e = t.getDate();
                return e > 10 && 20 > e ? "th" : ["st", "nd", "rd"][e % 10 - 1] || "th"
            },
            w: function(t, e) {
                return e.weekNumberCalculation(t)
            },
            W: function(t) {
                return D(t)
            }
        };
    Ee.dateFormatters = Ne, Ee.applyAll = I, Se.month = J, Se.basicWeek = V, Se.basicDay = U, n({
        weekMode: "fixed"
    }), Se.agendaWeek = $, Se.agendaDay = Q, n({
        allDaySlot: !0,
        allDayText: "all-day",
        firstHour: 6,
        slotMinutes: 30,
        defaultEventMinutes: 120,
        axisFormat: "h(:mm)tt",
        timeFormat: {
            agenda: "h:mm{ - h:mm}"
        },
        dragOpacity: {
            agenda: .5
        },
        minTime: 0,
        maxTime: 24,
        slotEventOverlap: !0
    })
})(jQuery);
! function(e) {
    function t(t, n, s) {
        var d = t.success,
            l = e.extend({}, t.data || {}, {
                "start-min": a(n, "u"),
                "start-max": a(s, "u"),
                singleevents: !0,
                "max-results": 9999
            }),
            f = t.currentTimezone;
        return f && (l.ctz = f = f.replace(" ", "_")), e.extend({}, t, {
            url: t.url.replace(/\/basic$/, "/full") + "?alt=json-in-script&callback=?",
            dataType: "jsonp",
            data: l,
            startParam: !1,
            endParam: !1,
            success: function(t) {
                var n = [];
                t.feed.entry && e.each(t.feed.entry, function(t, a) {
                    var o, s = a.gd$when[0].startTime,
                        d = i(s, !0),
                        l = i(a.gd$when[0].endTime, !0),
                        c = -1 == s.indexOf("T");
                    e.each(a.link, function(e, t) {
                        "text/html" == t.type && (o = t.href, f && (o += (-1 == o.indexOf("?") ? "?" : "&") + "ctz=" + f))
                    }), c && r(l, -1), n.push({
                        id: a.gCal$uid.value,
                        title: a.title.$t,
                        url: o,
                        start: d,
                        end: l,
                        allDay: c,
                        location: a.gd$where[0].valueString,
                        description: a.content.$t
                    })
                });
                var a = [n].concat(Array.prototype.slice.call(arguments, 1)),
                    s = o(d, this, a);
                return e.isArray(s) ? s : n
            }
        })
    }
    var n = e.fullCalendar,
        a = n.formatDate,
        i = n.parseISO8601,
        r = n.addDays,
        o = n.applyAll;
    n.sourceNormalizers.push(function(e) {
        ("gcal" == e.dataType || void 0 === e.dataType && (e.url || "").match(/^(http|https):\/\/www.google.com\/calendar\/feeds\//)) && (e.dataType = "gcal", void 0 === e.editable && (e.editable = !1))
    }), n.sourceFetchers.push(function(e, n, a) {
        return "gcal" == e.dataType ? t(e, n, a) : void 0
    }), n.gcalFeed = function(t, n) {
        return e.extend({}, n, {
            url: t,
            dataType: "gcal"
        })
    }
}(jQuery);

/*!
 * Easy pie chart is a jquery plugin to display simple animated pie charts for only one value
 *
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * Built on top of the jQuery library (http://jquery.com)
 *
 * @source: http://github.com/rendro/easy-pie-chart/
 * @autor: Robert Fleischmann
 * @version: 1.2.5
 *
 * Inspired by: http://dribbble.com/shots/631074-Simple-Pie-Charts-II?list=popular&offset=210
 * Thanks to Philip Thrasher for the jquery plugin boilerplate for coffee script
 */
! function(e) {
    return e.easyPieChart = function(t, n) {
        var i, a, r, o, s, l, d, c, u = this;
        return this.el = t, this.$el = e(t), this.$el.data("easyPieChart", this), this.init = function() {
            var t, i;
            return u.options = e.extend({}, e.easyPieChart.defaultOptions, n), t = parseInt(u.$el.data("percent"), 10), u.percentage = 0, u.canvas = e("<canvas width='" + u.options.size + "' height='" + u.options.size + "'></canvas>").get(0), u.$el.append(u.canvas), "undefined" != typeof G_vmlCanvasManager && null !== G_vmlCanvasManager && G_vmlCanvasManager.initElement(u.canvas), u.ctx = u.canvas.getContext("2d"), window.devicePixelRatio > 1 && (i = window.devicePixelRatio, e(u.canvas).css({
                width: u.options.size,
                height: u.options.size
            }), u.canvas.width *= i, u.canvas.height *= i, u.ctx.scale(i, i)), u.ctx.translate(u.options.size / 2, u.options.size / 2), u.ctx.rotate(u.options.rotate * Math.PI / 180), u.$el.addClass("easyPieChart"), u.$el.css({
                width: u.options.size,
                height: u.options.size,
                lineHeight: "" + u.options.size + "px"
            }), u.update(t), u
        }, this.update = function(e) {
            return e = parseFloat(e) || 0, u.options.animate === !1 ? r(e) : u.options.delay ? (a(u.percentage, 0), setTimeout(function() {
                return a(u.percentage, e)
            }, u.options.delay)) : a(u.percentage, e), u
        }, d = function() {
            var e, t, n;
            for (u.ctx.fillStyle = u.options.scaleColor, u.ctx.lineWidth = 1, n = [], e = t = 0; 24 >= t; e = ++t) n.push(i(e));
            return n
        }, i = function(e) {
            var t;
            t = 0 === e % 6 ? 0 : .017 * u.options.size, u.ctx.save(), u.ctx.rotate(e * Math.PI / 12), u.ctx.fillRect(u.options.size / 2 - t, 0, .05 * -u.options.size + t, 1), u.ctx.restore()
        }, c = function() {
            var e;
            e = u.options.size / 2 - u.options.lineWidth / 2, u.options.scaleColor !== !1 && (e -= .08 * u.options.size), u.ctx.beginPath(), u.ctx.arc(0, 0, e, 0, 2 * Math.PI, !0), u.ctx.closePath(), u.ctx.strokeStyle = u.options.trackColor, u.ctx.lineWidth = u.options.lineWidth, u.ctx.stroke()
        }, l = function() {
            u.options.scaleColor !== !1 && d(), u.options.trackColor !== !1 && c()
        }, r = function(t) {
            var n;
            l(), u.ctx.strokeStyle = e.isFunction(u.options.barColor) ? u.options.barColor(t) : u.options.barColor, u.ctx.lineCap = u.options.lineCap, u.ctx.lineWidth = u.options.lineWidth, n = u.options.size / 2 - u.options.lineWidth / 2, u.options.scaleColor !== !1 && (n -= .08 * u.options.size), u.ctx.save(), u.ctx.rotate(-Math.PI / 2), u.ctx.beginPath(), u.ctx.arc(0, 0, n, 0, 2 * Math.PI * t / 100, !1), u.ctx.stroke(), u.ctx.restore()
        }, s = function() {
            return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || function(e) {
                return window.setTimeout(e, 1e3 / 60)
            }
        }(), a = function(e, t) {
            var n, i;
            u.options.onStart.call(u), u.percentage = t, Date.now || (Date.now = function() {
                return +new Date
            }), i = Date.now(), n = function() {
                var a, d;
                return d = Math.min(Date.now() - i, u.options.animate), u.ctx.clearRect(-u.options.size / 2, -u.options.size / 2, u.options.size, u.options.size), l.call(u), a = [o(d, e, t - e, u.options.animate)], u.options.onStep.call(u, a), r.call(u, a), d >= u.options.animate ? u.options.onStop.call(u, a, t) : s(n)
            }, s(n)
        }, o = function(e, t, n, i) {
            var a, r;
            return a = function(e) {
                return Math.pow(e, 2)
            }, r = function(e) {
                return 1 > e ? a(e) : 2 - a(-2 * (e / 2) + 2)
            }, e /= i / 2, n / 2 * r(e) + t
        }, this.init()
    }, e.easyPieChart.defaultOptions = {
        barColor: "#ef1e25",
        trackColor: "#f2f2f2",
        scaleColor: "#dfe0e0",
        lineCap: "round",
        rotate: 0,
        size: 110,
        lineWidth: 3,
        animate: !1,
        delay: !1,
        onStart: e.noop,
        onStop: e.noop,
        onStep: e.noop
    }, e.fn.easyPieChart = function(t) {
        return e.each(this, function(n, i) {
            var a, r;
            return a = e(i), a.data("easyPieChart") ? void 0 : (r = e.extend({}, t, a.data()), a.data("easyPieChart", new e.easyPieChart(i, r)))
        })
    }, void 0
}(jQuery);

/*!
 * Javascript plotting library for jQuery, version 0.8.1.
 * Copyright (c) 2007-2013 IOLA and Ole Laursen.
 * Licensed under the MIT license.
 *
 * Plugin for jQuery for working with colors. Version 1.1.
 * Inspiration from jQuery color animation plugin by John Resig.
 * Released under the MIT license by Ole Laursen, October 2009.
 */
(function(e) {
    e.color = {}, e.color.make = function(t, n, r, i) {
        var s = {};
        return s.r = t || 0, s.g = n || 0, s.b = r || 0, s.a = i != null ? i : 1, s.add = function(e, t) {
            for (var n = 0; n < e.length; ++n) s[e.charAt(n)] += t;
            return s.normalize()
        }, s.scale = function(e, t) {
            for (var n = 0; n < e.length; ++n) s[e.charAt(n)] *= t;
            return s.normalize()
        }, s.toString = function() {
            return s.a >= 1 ? "rgb(" + [s.r, s.g, s.b].join(",") + ")" : "rgba(" + [s.r, s.g, s.b, s.a].join(",") + ")"
        }, s.normalize = function() {
            function e(e, t, n) {
                return t < e ? e : t > n ? n : t
            }
            return s.r = e(0, parseInt(s.r), 255), s.g = e(0, parseInt(s.g), 255), s.b = e(0, parseInt(s.b), 255), s.a = e(0, s.a, 1), s
        }, s.clone = function() {
            return e.color.make(s.r, s.b, s.g, s.a)
        }, s.normalize()
    }, e.color.extract = function(t, n) {
        var r;
        do {
            r = t.css(n).toLowerCase();
            if (r != "" && r != "transparent") break;
            t = t.parent()
        } while (!e.nodeName(t.get(0), "body"));
        return r == "rgba(0, 0, 0, 0)" && (r = "transparent"), e.color.parse(r)
    }, e.color.parse = function(n) {
        var r, i = e.color.make;
        if (r = /rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/.exec(n)) return i(parseInt(r[1], 10), parseInt(r[2], 10), parseInt(r[3], 10));
        if (r = /rgba\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]+(?:\.[0-9]+)?)\s*\)/.exec(n)) return i(parseInt(r[1], 10), parseInt(r[2], 10), parseInt(r[3], 10), parseFloat(r[4]));
        if (r = /rgb\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*\)/.exec(n)) return i(parseFloat(r[1]) * 2.55, parseFloat(r[2]) * 2.55, parseFloat(r[3]) * 2.55);
        if (r = /rgba\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\s*\)/.exec(n)) return i(parseFloat(r[1]) * 2.55, parseFloat(r[2]) * 2.55, parseFloat(r[3]) * 2.55, parseFloat(r[4]));
        if (r = /#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/.exec(n)) return i(parseInt(r[1], 16), parseInt(r[2], 16), parseInt(r[3], 16));
        if (r = /#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])/.exec(n)) return i(parseInt(r[1] + r[1], 16), parseInt(r[2] + r[2], 16), parseInt(r[3] + r[3], 16));
        var s = e.trim(n).toLowerCase();
        return s == "transparent" ? i(255, 255, 255, 0) : (r = t[s] || [0, 0, 0], i(r[0], r[1], r[2]))
    };
    var t = {
        aqua: [0, 255, 255],
        azure: [240, 255, 255],
        beige: [245, 245, 220],
        black: [0, 0, 0],
        blue: [0, 0, 255],
        brown: [165, 42, 42],
        cyan: [0, 255, 255],
        darkblue: [0, 0, 139],
        darkcyan: [0, 139, 139],
        darkgrey: [169, 169, 169],
        darkgreen: [0, 100, 0],
        darkkhaki: [189, 183, 107],
        darkmagenta: [139, 0, 139],
        darkolivegreen: [85, 107, 47],
        darkorange: [255, 140, 0],
        darkorchid: [153, 50, 204],
        darkred: [139, 0, 0],
        darksalmon: [233, 150, 122],
        darkviolet: [148, 0, 211],
        fuchsia: [255, 0, 255],
        gold: [255, 215, 0],
        green: [0, 128, 0],
        indigo: [75, 0, 130],
        khaki: [240, 230, 140],
        lightblue: [173, 216, 230],
        lightcyan: [224, 255, 255],
        lightgreen: [144, 238, 144],
        lightgrey: [211, 211, 211],
        lightpink: [255, 182, 193],
        lightyellow: [255, 255, 224],
        lime: [0, 255, 0],
        magenta: [255, 0, 255],
        maroon: [128, 0, 0],
        navy: [0, 0, 128],
        olive: [128, 128, 0],
        orange: [255, 165, 0],
        pink: [255, 192, 203],
        purple: [128, 0, 128],
        violet: [128, 0, 128],
        red: [255, 0, 0],
        silver: [192, 192, 192],
        white: [255, 255, 255],
        yellow: [255, 255, 0]
    }
})(jQuery),
function(e) {
    function n(t, n) {
        var r = n.children("." + t)[0];
        if (r == null) {
            r = document.createElement("canvas"), r.className = t, e(r).css({
                direction: "ltr",
                position: "absolute",
                left: 0,
                top: 0
            }).appendTo(n);
            if (!r.getContext) {
                if (!window.G_vmlCanvasManager) throw new Error("Canvas is not available. If you're using IE with a fall-back such as Excanvas, then there's either a mistake in your conditional include, or the page has no DOCTYPE and is rendering in Quirks Mode.");
                r = window.G_vmlCanvasManager.initElement(r)
            }
        }
        this.element = r;
        var i = this.context = r.getContext("2d"),
            s = window.devicePixelRatio || 1,
            o = i.webkitBackingStorePixelRatio || i.mozBackingStorePixelRatio || i.msBackingStorePixelRatio || i.oBackingStorePixelRatio || i.backingStorePixelRatio || 1;
        this.pixelRatio = s / o, this.resize(n.width(), n.height()), this.textContainer = null, this.text = {}, this._textCache = {}
    }

    function r(t, r, s, o) {
        function E(e, t) {
            t = [w].concat(t);
            for (var n = 0; n < e.length; ++n) e[n].apply(this, t)
        }

        function S() {
            var t = {
                Canvas: n
            };
            for (var r = 0; r < o.length; ++r) {
                var i = o[r];
                i.init(w, t), i.options && e.extend(!0, a, i.options)
            }
        }

        function x(n) {
            e.extend(!0, a, n), n && n.colors && (a.colors = n.colors), a.xaxis.color == null && (a.xaxis.color = e.color.parse(a.grid.color).scale("a", .22).toString()), a.yaxis.color == null && (a.yaxis.color = e.color.parse(a.grid.color).scale("a", .22).toString()), a.xaxis.tickColor == null && (a.xaxis.tickColor = a.grid.tickColor || a.xaxis.color), a.yaxis.tickColor == null && (a.yaxis.tickColor = a.grid.tickColor || a.yaxis.color), a.grid.borderColor == null && (a.grid.borderColor = a.grid.color), a.grid.tickColor == null && (a.grid.tickColor = e.color.parse(a.grid.color).scale("a", .22).toString());
            var r, i, s, o = {
                style: t.css("font-style"),
                size: Math.round(.8 * (+t.css("font-size").replace("px", "") || 13)),
                variant: t.css("font-variant"),
                weight: t.css("font-weight"),
                family: t.css("font-family")
            };
            o.lineHeight = o.size * 1.15, s = a.xaxes.length || 1;
            for (r = 0; r < s; ++r) i = a.xaxes[r], i && !i.tickColor && (i.tickColor = i.color), i = e.extend(!0, {}, a.xaxis, i), a.xaxes[r] = i, i.font && (i.font = e.extend({}, o, i.font), i.font.color || (i.font.color = i.color));
            s = a.yaxes.length || 1;
            for (r = 0; r < s; ++r) i = a.yaxes[r], i && !i.tickColor && (i.tickColor = i.color), i = e.extend(!0, {}, a.yaxis, i), a.yaxes[r] = i, i.font && (i.font = e.extend({}, o, i.font), i.font.color || (i.font.color = i.color));
            a.xaxis.noTicks && a.xaxis.ticks == null && (a.xaxis.ticks = a.xaxis.noTicks), a.yaxis.noTicks && a.yaxis.ticks == null && (a.yaxis.ticks = a.yaxis.noTicks), a.x2axis && (a.xaxes[1] = e.extend(!0, {}, a.xaxis, a.x2axis), a.xaxes[1].position = "top"), a.y2axis && (a.yaxes[1] = e.extend(!0, {}, a.yaxis, a.y2axis), a.yaxes[1].position = "right"), a.grid.coloredAreas && (a.grid.markings = a.grid.coloredAreas), a.grid.coloredAreasColor && (a.grid.markingsColor = a.grid.coloredAreasColor), a.lines && e.extend(!0, a.series.lines, a.lines), a.points && e.extend(!0, a.series.points, a.points), a.bars && e.extend(!0, a.series.bars, a.bars), a.shadowSize != null && (a.series.shadowSize = a.shadowSize), a.highlightColor != null && (a.series.highlightColor = a.highlightColor);
            for (r = 0; r < a.xaxes.length; ++r) O(d, r + 1).options = a.xaxes[r];
            for (r = 0; r < a.yaxes.length; ++r) O(v, r + 1).options = a.yaxes[r];
            for (var u in b) a.hooks[u] && a.hooks[u].length && (b[u] = b[u].concat(a.hooks[u]));
            E(b.processOptions, [a])
        }

        function T(e) {
            u = N(e), M(), _()
        }

        function N(t) {
            var n = [];
            for (var r = 0; r < t.length; ++r) {
                var i = e.extend(!0, {}, a.series);
                t[r].data != null ? (i.data = t[r].data, delete t[r].data, e.extend(!0, i, t[r]), t[r].data = i.data) : i.data = t[r], n.push(i)
            }
            return n
        }

        function C(e, t) {
            var n = e[t + "axis"];
            return typeof n == "object" && (n = n.n), typeof n != "number" && (n = 1), n
        }

        function k() {
            return e.grep(d.concat(v), function(e) {
                return e
            })
        }

        function L(e) {
            var t = {},
                n, r;
            for (n = 0; n < d.length; ++n) r = d[n], r && r.used && (t["x" + r.n] = r.c2p(e.left));
            for (n = 0; n < v.length; ++n) r = v[n], r && r.used && (t["y" + r.n] = r.c2p(e.top));
            return t.x1 !== undefined && (t.x = t.x1), t.y1 !== undefined && (t.y = t.y1), t
        }

        function A(e) {
            var t = {},
                n, r, i;
            for (n = 0; n < d.length; ++n) {
                r = d[n];
                if (r && r.used) {
                    i = "x" + r.n, e[i] == null && r.n == 1 && (i = "x");
                    if (e[i] != null) {
                        t.left = r.p2c(e[i]);
                        break
                    }
                }
            }
            for (n = 0; n < v.length; ++n) {
                r = v[n];
                if (r && r.used) {
                    i = "y" + r.n, e[i] == null && r.n == 1 && (i = "y");
                    if (e[i] != null) {
                        t.top = r.p2c(e[i]);
                        break
                    }
                }
            }
            return t
        }

        function O(t, n) {
            return t[n - 1] || (t[n - 1] = {
                n: n,
                direction: t == d ? "x" : "y",
                options: e.extend(!0, {}, t == d ? a.xaxis : a.yaxis)
            }), t[n - 1]
        }

        function M() {
            var t = u.length,
                n = -1,
                r;
            for (r = 0; r < u.length; ++r) {
                var i = u[r].color;
                i != null && (t--, typeof i == "number" && i > n && (n = i))
            }
            t <= n && (t = n + 1);
            var s, o = [],
                f = a.colors,
                l = f.length,
                c = 0;
            for (r = 0; r < t; r++) s = e.color.parse(f[r % l] || "#666"), r % l == 0 && r && (c >= 0 ? c < .5 ? c = -c - .2 : c = 0 : c = -c), o[r] = s.scale("rgb", 1 + c);
            var h = 0,
                p;
            for (r = 0; r < u.length; ++r) {
                p = u[r], p.color == null ? (p.color = o[h].toString(), ++h) : typeof p.color == "number" && (p.color = o[p.color].toString());
                if (p.lines.show == null) {
                    var m, g = !0;
                    for (m in p)
                        if (p[m] && p[m].show) {
                            g = !1;
                            break
                        }
                    g && (p.lines.show = !0)
                }
                p.lines.zero == null && (p.lines.zero = !!p.lines.fill), p.xaxis = O(d, C(p, "x")), p.yaxis = O(v, C(p, "y"))
            }
        }

        function _() {
            function x(e, t, n) {
                t < e.datamin && t != -r && (e.datamin = t), n > e.datamax && n != r && (e.datamax = n)
            }
            var t = Number.POSITIVE_INFINITY,
                n = Number.NEGATIVE_INFINITY,
                r = Number.MAX_VALUE,
                i, s, o, a, f, l, c, h, p, d, v, m, g, y, w, S;
            e.each(k(), function(e, r) {
                r.datamin = t, r.datamax = n, r.used = !1
            });
            for (i = 0; i < u.length; ++i) l = u[i], l.datapoints = {
                points: []
            }, E(b.processRawData, [l, l.data, l.datapoints]);
            for (i = 0; i < u.length; ++i) {
                l = u[i], w = l.data, S = l.datapoints.format;
                if (!S) {
                    S = [], S.push({
                        x: !0,
                        number: !0,
                        required: !0
                    }), S.push({
                        y: !0,
                        number: !0,
                        required: !0
                    });
                    if (l.bars.show || l.lines.show && l.lines.fill) {
                        var T = !!(l.bars.show && l.bars.zero || l.lines.show && l.lines.zero);
                        S.push({
                            y: !0,
                            number: !0,
                            required: !1,
                            defaultValue: 0,
                            autoscale: T
                        }), l.bars.horizontal && (delete S[S.length - 1].y, S[S.length - 1].x = !0)
                    }
                    l.datapoints.format = S
                }
                if (l.datapoints.pointsize != null) continue;
                l.datapoints.pointsize = S.length, h = l.datapoints.pointsize, c = l.datapoints.points;
                var N = l.lines.show && l.lines.steps;
                l.xaxis.used = l.yaxis.used = !0;
                for (s = o = 0; s < w.length; ++s, o += h) {
                    y = w[s];
                    var C = y == null;
                    if (!C)
                        for (a = 0; a < h; ++a) m = y[a], g = S[a], g && (g.number && m != null && (m = +m, isNaN(m) ? m = null : m == Infinity ? m = r : m == -Infinity && (m = -r)), m == null && (g.required && (C = !0), g.defaultValue != null && (m = g.defaultValue))), c[o + a] = m;
                    if (C)
                        for (a = 0; a < h; ++a) m = c[o + a], m != null && (g = S[a], g.autoscale && (g.x && x(l.xaxis, m, m), g.y && x(l.yaxis, m, m))), c[o + a] = null;
                    else if (N && o > 0 && c[o - h] != null && c[o - h] != c[o] && c[o - h + 1] != c[o + 1]) {
                        for (a = 0; a < h; ++a) c[o + h + a] = c[o + a];
                        c[o + 1] = c[o - h + 1], o += h
                    }
                }
            }
            for (i = 0; i < u.length; ++i) l = u[i], E(b.processDatapoints, [l, l.datapoints]);
            for (i = 0; i < u.length; ++i) {
                l = u[i], c = l.datapoints.points, h = l.datapoints.pointsize, S = l.datapoints.format;
                var L = t,
                    A = t,
                    O = n,
                    M = n;
                for (s = 0; s < c.length; s += h) {
                    if (c[s] == null) continue;
                    for (a = 0; a < h; ++a) {
                        m = c[s + a], g = S[a];
                        if (!g || g.autoscale === !1 || m == r || m == -r) continue;
                        g.x && (m < L && (L = m), m > O && (O = m)), g.y && (m < A && (A = m), m > M && (M = m))
                    }
                }
                if (l.bars.show) {
                    var _;
                    switch (l.bars.align) {
                        case "left":
                            _ = 0;
                            break;
                        case "right":
                            _ = -l.bars.barWidth;
                            break;
                        case "center":
                            _ = -l.bars.barWidth / 2;
                            break;
                        default:
                            throw new Error("Invalid bar alignment: " + l.bars.align)
                    }
                    l.bars.horizontal ? (A += _, M += _ + l.bars.barWidth) : (L += _, O += _ + l.bars.barWidth)
                }
                x(l.xaxis, L, O), x(l.yaxis, A, M)
            }
            e.each(k(), function(e, r) {
                r.datamin == t && (r.datamin = null), r.datamax == n && (r.datamax = null)
            })
        }

        function D() {
            t.css("padding", 0).children(":not(.flot-base,.flot-overlay)").remove(), t.css("position") == "static" && t.css("position", "relative"), f = new n("flot-base", t), l = new n("flot-overlay", t), h = f.context, p = l.context, c = e(l.element).unbind();
            var r = t.data("plot");
            r && (r.shutdown(), l.clear()), t.data("plot", w)
        }

        function P() {
            a.grid.hoverable && (c.mousemove(at), c.bind("mouseleave", ft)), a.grid.clickable && c.click(lt), E(b.bindEvents, [c])
        }

        function H() {
            ot && clearTimeout(ot), c.unbind("mousemove", at), c.unbind("mouseleave", ft), c.unbind("click", lt), E(b.shutdown, [c])
        }

        function B(e) {
            function t(e) {
                return e
            }
            var n, r, i = e.options.transform || t,
                s = e.options.inverseTransform;
            e.direction == "x" ? (n = e.scale = g / Math.abs(i(e.max) - i(e.min)), r = Math.min(i(e.max), i(e.min))) : (n = e.scale = y / Math.abs(i(e.max) - i(e.min)), n = -n, r = Math.max(i(e.max), i(e.min))), i == t ? e.p2c = function(e) {
                return (e - r) * n
            } : e.p2c = function(e) {
                return (i(e) - r) * n
            }, s ? e.c2p = function(e) {
                return s(r + e / n)
            } : e.c2p = function(e) {
                return r + e / n
            }
        }

        function j(e) {
            var t = e.options,
                n = e.ticks || [],
                r = t.labelWidth || 0,
                i = t.labelHeight || 0,
                s = r || e.direction == "x" ? Math.floor(f.width / (n.length || 1)) : null;
            legacyStyles = e.direction + "Axis " + e.direction + e.n + "Axis", layer = "flot-" + e.direction + "-axis flot-" + e.direction + e.n + "-axis " + legacyStyles, font = t.font || "flot-tick-label tickLabel";
            for (var o = 0; o < n.length; ++o) {
                var u = n[o];
                if (!u.label) continue;
                var a = f.getTextInfo(layer, u.label, font, null, s);
                r = Math.max(r, a.width), i = Math.max(i, a.height)
            }
            e.labelWidth = t.labelWidth || r, e.labelHeight = t.labelHeight || i
        }

        function F(t) {
            var n = t.labelWidth,
                r = t.labelHeight,
                i = t.options.position,
                s = t.options.tickLength,
                o = a.grid.axisMargin,
                u = a.grid.labelMargin,
                l = t.direction == "x" ? d : v,
                c, h, p = e.grep(l, function(e) {
                    return e && e.options.position == i && e.reserveSpace
                });
            e.inArray(t, p) == p.length - 1 && (o = 0);
            if (s == null) {
                var g = e.grep(l, function(e) {
                    return e && e.reserveSpace
                });
                h = e.inArray(t, g) == 0, h ? s = "full" : s = 5
            }
            isNaN(+s) || (u += +s), t.direction == "x" ? (r += u, i == "bottom" ? (m.bottom += r + o, t.box = {
                top: f.height - m.bottom,
                height: r
            }) : (t.box = {
                top: m.top + o,
                height: r
            }, m.top += r + o)) : (n += u, i == "left" ? (t.box = {
                left: m.left + o,
                width: n
            }, m.left += n + o) : (m.right += n + o, t.box = {
                left: f.width - m.right,
                width: n
            })), t.position = i, t.tickLength = s, t.box.padding = u, t.innermost = h
        }

        function I(e) {
            e.direction == "x" ? (e.box.left = m.left - e.labelWidth / 2, e.box.width = f.width - m.left - m.right + e.labelWidth) : (e.box.top = m.top - e.labelHeight / 2, e.box.height = f.height - m.bottom - m.top + e.labelHeight)
        }

        function q() {
            var t = a.grid.minBorderMargin,
                n = {
                    x: 0,
                    y: 0
                },
                r, i;
            if (t == null) {
                t = 0;
                for (r = 0; r < u.length; ++r) t = Math.max(t, 2 * (u[r].points.radius + u[r].points.lineWidth / 2))
            }
            n.x = n.y = Math.ceil(t), e.each(k(), function(e, t) {
                var r = t.direction;
                t.reserveSpace && (n[r] = Math.ceil(Math.max(n[r], (r == "x" ? t.labelWidth : t.labelHeight) / 2)))
            }), m.left = Math.max(n.x, m.left), m.right = Math.max(n.x, m.right), m.top = Math.max(n.y, m.top), m.bottom = Math.max(n.y, m.bottom)
        }

        function R() {
            var t, n = k(),
                r = a.grid.show;
            for (var i in m) {
                var s = a.grid.margin || 0;
                m[i] = typeof s == "number" ? s : s[i] || 0
            }
            E(b.processOffset, [m]);
            for (var i in m) typeof a.grid.borderWidth == "object" ? m[i] += r ? a.grid.borderWidth[i] : 0 : m[i] += r ? a.grid.borderWidth : 0;
            e.each(n, function(e, t) {
                t.show = t.options.show, t.show == null && (t.show = t.used), t.reserveSpace = t.show || t.options.reserveSpace, U(t)
            });
            if (r) {
                var o = e.grep(n, function(e) {
                    return e.reserveSpace
                });
                e.each(o, function(e, t) {
                    z(t), W(t), X(t, t.ticks), j(t)
                });
                for (t = o.length - 1; t >= 0; --t) F(o[t]);
                q(), e.each(o, function(e, t) {
                    I(t)
                })
            }
            g = f.width - m.left - m.right, y = f.height - m.bottom - m.top, e.each(n, function(e, t) {
                B(t)
            }), r && G(), it()
        }

        function U(e) {
            var t = e.options,
                n = +(t.min != null ? t.min : e.datamin),
                r = +(t.max != null ? t.max : e.datamax),
                i = r - n;
            if (i == 0) {
                var s = r == 0 ? 1 : .01;
                t.min == null && (n -= s);
                if (t.max == null || t.min != null) r += s
            } else {
                var o = t.autoscaleMargin;
                o != null && (t.min == null && (n -= i * o, n < 0 && e.datamin != null && e.datamin >= 0 && (n = 0)), t.max == null && (r += i * o, r > 0 && e.datamax != null && e.datamax <= 0 && (r = 0)))
            }
            e.min = n, e.max = r
        }

        function z(t) {
            var n = t.options,
                r;
            typeof n.ticks == "number" && n.ticks > 0 ? r = n.ticks : r = .3 * Math.sqrt(t.direction == "x" ? f.width : f.height);
            var s = (t.max - t.min) / r,
                o = -Math.floor(Math.log(s) / Math.LN10),
                u = n.tickDecimals;
            u != null && o > u && (o = u);
            var a = Math.pow(10, -o),
                l = s / a,
                c;
            l < 1.5 ? c = 1 : l < 3 ? (c = 2, l > 2.25 && (u == null || o + 1 <= u) && (c = 2.5, ++o)) : l < 7.5 ? c = 5 : c = 10, c *= a, n.minTickSize != null && c < n.minTickSize && (c = n.minTickSize), t.delta = s, t.tickDecimals = Math.max(0, u != null ? u : o), t.tickSize = n.tickSize || c;
            if (n.mode == "time" && !t.tickGenerator) throw new Error("Time mode requires the flot.time plugin.");
            t.tickGenerator || (t.tickGenerator = function(e) {
                var t = [],
                    n = i(e.min, e.tickSize),
                    r = 0,
                    s = Number.NaN,
                    o;
                do o = s, s = n + r * e.tickSize, t.push(s), ++r; while (s < e.max && s != o);
                return t
            }, t.tickFormatter = function(e, t) {
                var n = t.tickDecimals ? Math.pow(10, t.tickDecimals) : 1,
                    r = "" + Math.round(e * n) / n;
                if (t.tickDecimals != null) {
                    var i = r.indexOf("."),
                        s = i == -1 ? 0 : r.length - i - 1;
                    if (s < t.tickDecimals) return (s ? r : r + ".") + ("" + n).substr(1, t.tickDecimals - s)
                }
                return r
            }), e.isFunction(n.tickFormatter) && (t.tickFormatter = function(e, t) {
                return "" + n.tickFormatter(e, t)
            });
            if (n.alignTicksWithAxis != null) {
                var h = (t.direction == "x" ? d : v)[n.alignTicksWithAxis - 1];
                if (h && h.used && h != t) {
                    var p = t.tickGenerator(t);
                    p.length > 0 && (n.min == null && (t.min = Math.min(t.min, p[0])), n.max == null && p.length > 1 && (t.max = Math.max(t.max, p[p.length - 1]))), t.tickGenerator = function(e) {
                        var t = [],
                            n, r;
                        for (r = 0; r < h.ticks.length; ++r) n = (h.ticks[r].v - h.min) / (h.max - h.min), n = e.min + n * (e.max - e.min), t.push(n);
                        return t
                    };
                    if (!t.mode && n.tickDecimals == null) {
                        var m = Math.max(0, -Math.floor(Math.log(t.delta) / Math.LN10) + 1),
                            g = t.tickGenerator(t);
                        g.length > 1 && /\..*0$/.test((g[1] - g[0]).toFixed(m)) || (t.tickDecimals = m)
                    }
                }
            }
        }

        function W(t) {
            var n = t.options.ticks,
                r = [];
            n == null || typeof n == "number" && n > 0 ? r = t.tickGenerator(t) : n && (e.isFunction(n) ? r = n(t) : r = n);
            var i, s;
            t.ticks = [];
            for (i = 0; i < r.length; ++i) {
                var o = null,
                    u = r[i];
                typeof u == "object" ? (s = +u[0], u.length > 1 && (o = u[1])) : s = +u, o == null && (o = t.tickFormatter(s, t)), isNaN(s) || t.ticks.push({
                    v: s,
                    label: o
                })
            }
        }

        function X(e, t) {
            e.options.autoscaleMargin && t.length > 0 && (e.options.min == null && (e.min = Math.min(e.min, t[0].v)), e.options.max == null && t.length > 1 && (e.max = Math.max(e.max, t[t.length - 1].v)))
        }

        function V() {
            f.clear(), E(b.drawBackground, [h]);
            var e = a.grid;
            e.show && e.backgroundColor && K(), e.show && !e.aboveData && Q();
            for (var t = 0; t < u.length; ++t) E(b.drawSeries, [h, u[t]]), Y(u[t]);
            E(b.draw, [h]), e.show && e.aboveData && Q(), f.render(), ht()
        }

        function J(e, t) {
            var n, r, i, s, o = k();
            for (var u = 0; u < o.length; ++u) {
                n = o[u];
                if (n.direction == t) {
                    s = t + n.n + "axis", !e[s] && n.n == 1 && (s = t + "axis");
                    if (e[s]) {
                        r = e[s].from, i = e[s].to;
                        break
                    }
                }
            }
            e[s] || (n = t == "x" ? d[0] : v[0], r = e[t + "1"], i = e[t + "2"]);
            if (r != null && i != null && r > i) {
                var a = r;
                r = i, i = a
            }
            return {
                from: r,
                to: i,
                axis: n
            }
        }

        function K() {
            h.save(), h.translate(m.left, m.top), h.fillStyle = bt(a.grid.backgroundColor, y, 0, "rgba(255, 255, 255, 0)"), h.fillRect(0, 0, g, y), h.restore()
        }

        function Q() {
            var t, n, r, i;
            h.save(), h.translate(m.left, m.top);
            var s = a.grid.markings;
            if (s) {
                e.isFunction(s) && (n = w.getAxes(), n.xmin = n.xaxis.min, n.xmax = n.xaxis.max, n.ymin = n.yaxis.min, n.ymax = n.yaxis.max, s = s(n));
                for (t = 0; t < s.length; ++t) {
                    var o = s[t],
                        u = J(o, "x"),
                        f = J(o, "y");
                    u.from == null && (u.from = u.axis.min), u.to == null && (u.to = u.axis.max), f.from == null && (f.from = f.axis.min), f.to == null && (f.to = f.axis.max);
                    if (u.to < u.axis.min || u.from > u.axis.max || f.to < f.axis.min || f.from > f.axis.max) continue;
                    u.from = Math.max(u.from, u.axis.min), u.to = Math.min(u.to, u.axis.max), f.from = Math.max(f.from, f.axis.min), f.to = Math.min(f.to, f.axis.max);
                    if (u.from == u.to && f.from == f.to) continue;
                    u.from = u.axis.p2c(u.from), u.to = u.axis.p2c(u.to), f.from = f.axis.p2c(f.from), f.to = f.axis.p2c(f.to), u.from == u.to || f.from == f.to ? (h.beginPath(), h.strokeStyle = o.color || a.grid.markingsColor, h.lineWidth = o.lineWidth || a.grid.markingsLineWidth, h.moveTo(u.from, f.from), h.lineTo(u.to, f.to), h.stroke()) : (h.fillStyle = o.color || a.grid.markingsColor, h.fillRect(u.from, f.to, u.to - u.from, f.from - f.to))
                }
            }
            n = k(), r = a.grid.borderWidth;
            for (var l = 0; l < n.length; ++l) {
                var c = n[l],
                    p = c.box,
                    d = c.tickLength,
                    v, b, E, S;
                if (!c.show || c.ticks.length == 0) continue;
                h.lineWidth = 1, c.direction == "x" ? (v = 0, d == "full" ? b = c.position == "top" ? 0 : y : b = p.top - m.top + (c.position == "top" ? p.height : 0)) : (b = 0, d == "full" ? v = c.position == "left" ? 0 : g : v = p.left - m.left + (c.position == "left" ? p.width : 0)), c.innermost || (h.strokeStyle = c.options.color, h.beginPath(), E = S = 0, c.direction == "x" ? E = g + 1 : S = y + 1, h.lineWidth == 1 && (c.direction == "x" ? b = Math.floor(b) + .5 : v = Math.floor(v) + .5), h.moveTo(v, b), h.lineTo(v + E, b + S), h.stroke()), h.strokeStyle = c.options.tickColor, h.beginPath();
                for (t = 0; t < c.ticks.length; ++t) {
                    var x = c.ticks[t].v;
                    E = S = 0;
                    if (isNaN(x) || x < c.min || x > c.max || d == "full" && (typeof r == "object" && r[c.position] > 0 || r > 0) && (x == c.min || x == c.max)) continue;
                    c.direction == "x" ? (v = c.p2c(x), S = d == "full" ? -y : d, c.position == "top" && (S = -S)) : (b = c.p2c(x), E = d == "full" ? -g : d, c.position == "left" && (E = -E)), h.lineWidth == 1 && (c.direction == "x" ? v = Math.floor(v) + .5 : b = Math.floor(b) + .5), h.moveTo(v, b), h.lineTo(v + E, b + S)
                }
                h.stroke()
            }
            r && (i = a.grid.borderColor, typeof r == "object" || typeof i == "object" ? (typeof r != "object" && (r = {
                top: r,
                right: r,
                bottom: r,
                left: r
            }), typeof i != "object" && (i = {
                top: i,
                right: i,
                bottom: i,
                left: i
            }), r.top > 0 && (h.strokeStyle = i.top, h.lineWidth = r.top, h.beginPath(), h.moveTo(0 - r.left, 0 - r.top / 2), h.lineTo(g, 0 - r.top / 2), h.stroke()), r.right > 0 && (h.strokeStyle = i.right, h.lineWidth = r.right, h.beginPath(), h.moveTo(g + r.right / 2, 0 - r.top), h.lineTo(g + r.right / 2, y), h.stroke()), r.bottom > 0 && (h.strokeStyle = i.bottom, h.lineWidth = r.bottom, h.beginPath(), h.moveTo(g + r.right, y + r.bottom / 2), h.lineTo(0, y + r.bottom / 2), h.stroke()), r.left > 0 && (h.strokeStyle = i.left, h.lineWidth = r.left, h.beginPath(), h.moveTo(0 - r.left / 2, y + r.bottom), h.lineTo(0 - r.left / 2, 0), h.stroke())) : (h.lineWidth = r, h.strokeStyle = a.grid.borderColor, h.strokeRect(-r / 2, -r / 2, g + r, y + r))), h.restore()
        }

        function G() {
            e.each(k(), function(e, t) {
                if (!t.show || t.ticks.length == 0) return;
                var n = t.box,
                    r = t.direction + "Axis " + t.direction + t.n + "Axis",
                    i = "flot-" + t.direction + "-axis flot-" + t.direction + t.n + "-axis " + r,
                    s = t.options.font || "flot-tick-label tickLabel",
                    o, u, a, l, c;
                f.removeText(i);
                for (var h = 0; h < t.ticks.length; ++h) {
                    o = t.ticks[h];
                    if (!o.label || o.v < t.min || o.v > t.max) continue;
                    t.direction == "x" ? (l = "center", u = m.left + t.p2c(o.v), t.position == "bottom" ? a = n.top + n.padding : (a = n.top + n.height - n.padding, c = "bottom")) : (c = "middle", a = m.top + t.p2c(o.v), t.position == "left" ? (u = n.left + n.width - n.padding, l = "right") : u = n.left + n.padding), f.addText(i, u, a, o.label, s, null, null, l, c)
                }
            })
        }

        function Y(e) {
            e.lines.show && Z(e), e.bars.show && nt(e), e.points.show && et(e)
        }

        function Z(e) {
            function t(e, t, n, r, i) {
                var s = e.points,
                    o = e.pointsize,
                    u = null,
                    a = null;
                h.beginPath();
                for (var f = o; f < s.length; f += o) {
                    var l = s[f - o],
                        c = s[f - o + 1],
                        p = s[f],
                        d = s[f + 1];
                    if (l == null || p == null) continue;
                    if (c <= d && c < i.min) {
                        if (d < i.min) continue;
                        l = (i.min - c) / (d - c) * (p - l) + l, c = i.min
                    } else if (d <= c && d < i.min) {
                        if (c < i.min) continue;
                        p = (i.min - c) / (d - c) * (p - l) + l, d = i.min
                    }
                    if (c >= d && c > i.max) {
                        if (d > i.max) continue;
                        l = (i.max - c) / (d - c) * (p - l) + l, c = i.max
                    } else if (d >= c && d > i.max) {
                        if (c > i.max) continue;
                        p = (i.max - c) / (d - c) * (p - l) + l, d = i.max
                    }
                    if (l <= p && l < r.min) {
                        if (p < r.min) continue;
                        c = (r.min - l) / (p - l) * (d - c) + c, l = r.min
                    } else if (p <= l && p < r.min) {
                        if (l < r.min) continue;
                        d = (r.min - l) / (p - l) * (d - c) + c, p = r.min
                    }
                    if (l >= p && l > r.max) {
                        if (p > r.max) continue;
                        c = (r.max - l) / (p - l) * (d - c) + c, l = r.max
                    } else if (p >= l && p > r.max) {
                        if (l > r.max) continue;
                        d = (r.max - l) / (p - l) * (d - c) + c, p = r.max
                    }(l != u || c != a) && h.moveTo(r.p2c(l) + t, i.p2c(c) + n), u = p, a = d, h.lineTo(r.p2c(p) + t, i.p2c(d) + n)
                }
                h.stroke()
            }

            function n(e, t, n) {
                var r = e.points,
                    i = e.pointsize,
                    s = Math.min(Math.max(0, n.min), n.max),
                    o = 0,
                    u, a = !1,
                    f = 1,
                    l = 0,
                    c = 0;
                for (;;) {
                    if (i > 0 && o > r.length + i) break;
                    o += i;
                    var p = r[o - i],
                        d = r[o - i + f],
                        v = r[o],
                        m = r[o + f];
                    if (a) {
                        if (i > 0 && p != null && v == null) {
                            c = o, i = -i, f = 2;
                            continue
                        }
                        if (i < 0 && o == l + i) {
                            h.fill(), a = !1, i = -i, f = 1, o = l = c + i;
                            continue
                        }
                    }
                    if (p == null || v == null) continue;
                    if (p <= v && p < t.min) {
                        if (v < t.min) continue;
                        d = (t.min - p) / (v - p) * (m - d) + d, p = t.min
                    } else if (v <= p && v < t.min) {
                        if (p < t.min) continue;
                        m = (t.min - p) / (v - p) * (m - d) + d, v = t.min
                    }
                    if (p >= v && p > t.max) {
                        if (v > t.max) continue;
                        d = (t.max - p) / (v - p) * (m - d) + d, p = t.max
                    } else if (v >= p && v > t.max) {
                        if (p > t.max) continue;
                        m = (t.max - p) / (v - p) * (m - d) + d, v = t.max
                    }
                    a || (h.beginPath(), h.moveTo(t.p2c(p), n.p2c(s)), a = !0);
                    if (d >= n.max && m >= n.max) {
                        h.lineTo(t.p2c(p), n.p2c(n.max)), h.lineTo(t.p2c(v), n.p2c(n.max));
                        continue
                    }
                    if (d <= n.min && m <= n.min) {
                        h.lineTo(t.p2c(p), n.p2c(n.min)), h.lineTo(t.p2c(v), n.p2c(n.min));
                        continue
                    }
                    var g = p,
                        y = v;
                    d <= m && d < n.min && m >= n.min ? (p = (n.min - d) / (m - d) * (v - p) + p, d = n.min) : m <= d && m < n.min && d >= n.min && (v = (n.min - d) / (m - d) * (v - p) + p, m = n.min), d >= m && d > n.max && m <= n.max ? (p = (n.max - d) / (m - d) * (v - p) + p, d = n.max) : m >= d && m > n.max && d <= n.max && (v = (n.max - d) / (m - d) * (v - p) + p, m = n.max), p != g && h.lineTo(t.p2c(g), n.p2c(d)), h.lineTo(t.p2c(p), n.p2c(d)), h.lineTo(t.p2c(v), n.p2c(m)), v != y && (h.lineTo(t.p2c(v), n.p2c(m)), h.lineTo(t.p2c(y), n.p2c(m)))
                }
            }
            h.save(), h.translate(m.left, m.top), h.lineJoin = "round";
            var r = e.lines.lineWidth,
                i = e.shadowSize;
            if (r > 0 && i > 0) {
                h.lineWidth = i, h.strokeStyle = "rgba(0,0,0,0.1)";
                var s = Math.PI / 18;
                t(e.datapoints, Math.sin(s) * (r / 2 + i / 2), Math.cos(s) * (r / 2 + i / 2), e.xaxis, e.yaxis), h.lineWidth = i / 2, t(e.datapoints, Math.sin(s) * (r / 2 + i / 4), Math.cos(s) * (r / 2 + i / 4), e.xaxis, e.yaxis)
            }
            h.lineWidth = r, h.strokeStyle = e.color;
            var o = rt(e.lines, e.color, 0, y);
            o && (h.fillStyle = o, n(e.datapoints, e.xaxis, e.yaxis)), r > 0 && t(e.datapoints, 0, 0, e.xaxis, e.yaxis), h.restore()
        }

        function et(e) {
            function t(e, t, n, r, i, s, o, u) {
                var a = e.points,
                    f = e.pointsize;
                for (var l = 0; l < a.length; l += f) {
                    var c = a[l],
                        p = a[l + 1];
                    if (c == null || c < s.min || c > s.max || p < o.min || p > o.max) continue;
                    h.beginPath(), c = s.p2c(c), p = o.p2c(p) + r, u == "circle" ? h.arc(c, p, t, 0, i ? Math.PI : Math.PI * 2, !1) : u(h, c, p, t, i), h.closePath(), n && (h.fillStyle = n, h.fill()), h.stroke()
                }
            }
            h.save(), h.translate(m.left, m.top);
            var n = e.points.lineWidth,
                r = e.shadowSize,
                i = e.points.radius,
                s = e.points.symbol;
            n == 0 && (n = 1e-4);
            if (n > 0 && r > 0) {
                var o = r / 2;
                h.lineWidth = o, h.strokeStyle = "rgba(0,0,0,0.1)", t(e.datapoints, i, null, o + o / 2, !0, e.xaxis, e.yaxis, s), h.strokeStyle = "rgba(0,0,0,0.2)", t(e.datapoints, i, null, o / 2, !0, e.xaxis, e.yaxis, s)
            }
            h.lineWidth = n, h.strokeStyle = e.color, t(e.datapoints, i, rt(e.points, e.color), 0, !1, e.xaxis, e.yaxis, s), h.restore()
        }

        function tt(e, t, n, r, i, s, o, u, a, f, l, c) {
            var h, p, d, v, m, g, y, b, w;
            l ? (b = g = y = !0, m = !1, h = n, p = e, v = t + r, d = t + i, p < h && (w = p, p = h, h = w, m = !0, g = !1)) : (m = g = y = !0, b = !1, h = e + r, p = e + i, d = n, v = t, v < d && (w = v, v = d, d = w, b = !0, y = !1));
            if (p < u.min || h > u.max || v < a.min || d > a.max) return;
            h < u.min && (h = u.min, m = !1), p > u.max && (p = u.max, g = !1), d < a.min && (d = a.min, b = !1), v > a.max && (v = a.max, y = !1), h = u.p2c(h), d = a.p2c(d), p = u.p2c(p), v = a.p2c(v), o && (f.beginPath(), f.moveTo(h, d), f.lineTo(h, v), f.lineTo(p, v), f.lineTo(p, d), f.fillStyle = o(d, v), f.fill()), c > 0 && (m || g || y || b) && (f.beginPath(), f.moveTo(h, d + s), m ? f.lineTo(h, v + s) : f.moveTo(h, v + s), y ? f.lineTo(p, v + s) : f.moveTo(p, v + s), g ? f.lineTo(p, d + s) : f.moveTo(p, d + s), b ? f.lineTo(h, d + s) : f.moveTo(h, d + s), f.stroke())
        }

        function nt(e) {
            function t(t, n, r, i, s, o, u) {
                var a = t.points,
                    f = t.pointsize;
                for (var l = 0; l < a.length; l += f) {
                    if (a[l] == null) continue;
                    tt(a[l], a[l + 1], a[l + 2], n, r, i, s, o, u, h, e.bars.horizontal, e.bars.lineWidth)
                }
            }
            h.save(), h.translate(m.left, m.top), h.lineWidth = e.bars.lineWidth, h.strokeStyle = e.color;
            var n;
            switch (e.bars.align) {
                case "left":
                    n = 0;
                    break;
                case "right":
                    n = -e.bars.barWidth;
                    break;
                case "center":
                    n = -e.bars.barWidth / 2;
                    break;
                default:
                    throw new Error("Invalid bar alignment: " + e.bars.align)
            }
            var r = e.bars.fill ? function(t, n) {
                return rt(e.bars, e.color, t, n)
            } : null;
            t(e.datapoints, n, n + e.bars.barWidth, 0, r, e.xaxis, e.yaxis), h.restore()
        }

        function rt(t, n, r, i) {
            var s = t.fill;
            if (!s) return null;
            if (t.fillColor) return bt(t.fillColor, r, i, n);
            var o = e.color.parse(n);
            return o.a = typeof s == "number" ? s : .4, o.normalize(), o.toString()
        }

        function it() {
            t.find(".legend").remove();
            if (!a.legend.show) return;
            var n = [],
                r = [],
                i = !1,
                s = a.legend.labelFormatter,
                o, f;
            for (var l = 0; l < u.length; ++l) o = u[l], o.label && (f = s ? s(o.label, o) : o.label, f && r.push({
                label: f,
                color: o.color
            }));
            if (a.legend.sorted)
                if (e.isFunction(a.legend.sorted)) r.sort(a.legend.sorted);
                else if (a.legend.sorted == "reverse") r.reverse();
            else {
                var c = a.legend.sorted != "descending";
                r.sort(function(e, t) {
                    return e.label == t.label ? 0 : e.label < t.label != c ? 1 : -1
                })
            }
            for (var l = 0; l < r.length; ++l) {
                var h = r[l];
                l % a.legend.noColumns == 0 && (i && n.push("</tr>"), n.push("<tr>"), i = !0), n.push('<td class="legendColorBox"><div style="border:1px solid ' + a.legend.labelBoxBorderColor + ';padding:1px"><div style="width:4px;height:0;border:5px solid ' + h.color + ';overflow:hidden"></div></div></td>' + '<td class="legendLabel">' + h.label + "</td>")
            }
            i && n.push("</tr>");
            if (n.length == 0) return;
            var p = '<table style="font-size:smaller;color:' + a.grid.color + '">' + n.join("") + "</table>";
            if (a.legend.container != null) e(a.legend.container).html(p);
            else {
                var d = "",
                    v = a.legend.position,
                    g = a.legend.margin;
                g[0] == null && (g = [g, g]), v.charAt(0) == "n" ? d += "top:" + (g[1] + m.top) + "px;" : v.charAt(0) == "s" && (d += "bottom:" + (g[1] + m.bottom) + "px;"), v.charAt(1) == "e" ? d += "right:" + (g[0] + m.right) + "px;" : v.charAt(1) == "w" && (d += "left:" + (g[0] + m.left) + "px;");
                var y = e('<div class="legend">' + p.replace('style="', 'style="position:absolute;' + d + ";") + "</div>").appendTo(t);
                if (a.legend.backgroundOpacity != 0) {
                    var b = a.legend.backgroundColor;
                    b == null && (b = a.grid.backgroundColor, b && typeof b == "string" ? b = e.color.parse(b) : b = e.color.extract(y, "background-color"), b.a = 1, b = b.toString());
                    var w = y.children();
                    e('<div style="position:absolute;width:' + w.width() + "px;height:" + w.height() + "px;" + d + "background-color:" + b + ';"> </div>').prependTo(y).css("opacity", a.legend.backgroundOpacity)
                }
            }
        }

        function ut(e, t, n) {
            var r = a.grid.mouseActiveRadius,
                i = r * r + 1,
                s = null,
                o = !1,
                f, l, c;
            for (f = u.length - 1; f >= 0; --f) {
                if (!n(u[f])) continue;
                var h = u[f],
                    p = h.xaxis,
                    d = h.yaxis,
                    v = h.datapoints.points,
                    m = p.c2p(e),
                    g = d.c2p(t),
                    y = r / p.scale,
                    b = r / d.scale;
                c = h.datapoints.pointsize, p.options.inverseTransform && (y = Number.MAX_VALUE), d.options.inverseTransform && (b = Number.MAX_VALUE);
                if (h.lines.show || h.points.show)
                    for (l = 0; l < v.length; l += c) {
                        var w = v[l],
                            E = v[l + 1];
                        if (w == null) continue;
                        if (w - m > y || w - m < -y || E - g > b || E - g < -b) continue;
                        var S = Math.abs(p.p2c(w) - e),
                            x = Math.abs(d.p2c(E) - t),
                            T = S * S + x * x;
                        T < i && (i = T, s = [f, l / c])
                    }
                if (h.bars.show && !s) {
                    var N = h.bars.align == "left" ? 0 : -h.bars.barWidth / 2,
                        C = N + h.bars.barWidth;
                    for (l = 0; l < v.length; l += c) {
                        var w = v[l],
                            E = v[l + 1],
                            k = v[l + 2];
                        if (w == null) continue;
                        if (u[f].bars.horizontal ? m <= Math.max(k, w) && m >= Math.min(k, w) && g >= E + N && g <= E + C : m >= w + N && m <= w + C && g >= Math.min(k, E) && g <= Math.max(k, E)) s = [f, l / c]
                    }
                }
            }
            return s ? (f = s[0], l = s[1], c = u[f].datapoints.pointsize, {
                datapoint: u[f].datapoints.points.slice(l * c, (l + 1) * c),
                dataIndex: l,
                series: u[f],
                seriesIndex: f
            }) : null
        }

        function at(e) {
            a.grid.hoverable && ct("plothover", e, function(e) {
                return e["hoverable"] != 0
            })
        }

        function ft(e) {
            a.grid.hoverable && ct("plothover", e, function(e) {
                return !1
            })
        }

        function lt(e) {
            ct("plotclick", e, function(e) {
                return e["clickable"] != 0
            })
        }

        function ct(e, n, r) {
            var i = c.offset(),
                s = n.pageX - i.left - m.left,
                o = n.pageY - i.top - m.top,
                u = L({
                    left: s,
                    top: o
                });
            u.pageX = n.pageX, u.pageY = n.pageY;
            var f = ut(s, o, r);
            f && (f.pageX = parseInt(f.series.xaxis.p2c(f.datapoint[0]) + i.left + m.left, 10), f.pageY = parseInt(f.series.yaxis.p2c(f.datapoint[1]) + i.top + m.top, 10));
            if (a.grid.autoHighlight) {
                for (var l = 0; l < st.length; ++l) {
                    var h = st[l];
                    h.auto == e && (!f || h.series != f.series || h.point[0] != f.datapoint[0] || h.point[1] != f.datapoint[1]) && vt(h.series, h.point)
                }
                f && dt(f.series, f.datapoint, e)
            }
            t.trigger(e, [u, f])
        }

        function ht() {
            var e = a.interaction.redrawOverlayInterval;
            if (e == -1) {
                pt();
                return
            }
            ot || (ot = setTimeout(pt, e))
        }

        function pt() {
            ot = null, p.save(), l.clear(), p.translate(m.left, m.top);
            var e, t;
            for (e = 0; e < st.length; ++e) t = st[e], t.series.bars.show ? yt(t.series, t.point) : gt(t.series, t.point);
            p.restore(), E(b.drawOverlay, [p])
        }

        function dt(e, t, n) {
            typeof e == "number" && (e = u[e]);
            if (typeof t == "number") {
                var r = e.datapoints.pointsize;
                t = e.datapoints.points.slice(r * t, r * (t + 1))
            }
            var i = mt(e, t);
            i == -1 ? (st.push({
                series: e,
                point: t,
                auto: n
            }), ht()) : n || (st[i].auto = !1)
        }

        function vt(e, t) {
            if (e == null && t == null) {
                st = [], ht();
                return
            }
            typeof e == "number" && (e = u[e]);
            if (typeof t == "number") {
                var n = e.datapoints.pointsize;
                t = e.datapoints.points.slice(n * t, n * (t + 1))
            }
            var r = mt(e, t);
            r != -1 && (st.splice(r, 1), ht())
        }

        function mt(e, t) {
            for (var n = 0; n < st.length; ++n) {
                var r = st[n];
                if (r.series == e && r.point[0] == t[0] && r.point[1] == t[1]) return n
            }
            return -1
        }

        function gt(t, n) {
            var r = n[0],
                i = n[1],
                s = t.xaxis,
                o = t.yaxis,
                u = typeof t.highlightColor == "string" ? t.highlightColor : e.color.parse(t.color).scale("a", .5).toString();
            if (r < s.min || r > s.max || i < o.min || i > o.max) return;
            var a = t.points.radius + t.points.lineWidth / 2;
            p.lineWidth = a, p.strokeStyle = u;
            var f = 1.5 * a;
            r = s.p2c(r), i = o.p2c(i), p.beginPath(), t.points.symbol == "circle" ? p.arc(r, i, f, 0, 2 * Math.PI, !1) : t.points.symbol(p, r, i, f, !1), p.closePath(), p.stroke()
        }

        function yt(t, n) {
            var r = typeof t.highlightColor == "string" ? t.highlightColor : e.color.parse(t.color).scale("a", .5).toString(),
                i = r,
                s = t.bars.align == "left" ? 0 : -t.bars.barWidth / 2;
            p.lineWidth = t.bars.lineWidth, p.strokeStyle = r, tt(n[0], n[1], n[2] || 0, s, s + t.bars.barWidth, 0, function() {
                return i
            }, t.xaxis, t.yaxis, p, t.bars.horizontal, t.bars.lineWidth)
        }

        function bt(t, n, r, i) {
            if (typeof t == "string") return t;
            var s = h.createLinearGradient(0, r, 0, n);
            for (var o = 0, u = t.colors.length; o < u; ++o) {
                var a = t.colors[o];
                if (typeof a != "string") {
                    var f = e.color.parse(i);
                    a.brightness != null && (f = f.scale("rgb", a.brightness)), a.opacity != null && (f.a *= a.opacity), a = f.toString()
                }
                s.addColorStop(o / (u - 1), a)
            }
            return s
        }
        var u = [],
            a = {
                colors: ["#edc240", "#afd8f8", "#cb4b4b", "#4da74d", "#9440ed"],
                legend: {
                    show: !0,
                    noColumns: 1,
                    labelFormatter: null,
                    labelBoxBorderColor: "#ccc",
                    container: null,
                    position: "ne",
                    margin: 5,
                    backgroundColor: null,
                    backgroundOpacity: .85,
                    sorted: null
                },
                xaxis: {
                    show: null,
                    position: "bottom",
                    mode: null,
                    font: null,
                    color: null,
                    tickColor: null,
                    transform: null,
                    inverseTransform: null,
                    min: null,
                    max: null,
                    autoscaleMargin: null,
                    ticks: null,
                    tickFormatter: null,
                    labelWidth: null,
                    labelHeight: null,
                    reserveSpace: null,
                    tickLength: null,
                    alignTicksWithAxis: null,
                    tickDecimals: null,
                    tickSize: null,
                    minTickSize: null
                },
                yaxis: {
                    autoscaleMargin: .02,
                    position: "left"
                },
                xaxes: [],
                yaxes: [],
                series: {
                    points: {
                        show: !1,
                        radius: 3,
                        lineWidth: 2,
                        fill: !0,
                        fillColor: "#ffffff",
                        symbol: "circle"
                    },
                    lines: {
                        lineWidth: 2,
                        fill: !1,
                        fillColor: null,
                        steps: !1
                    },
                    bars: {
                        show: !1,
                        lineWidth: 2,
                        barWidth: 1,
                        fill: !0,
                        fillColor: null,
                        align: "left",
                        horizontal: !1,
                        zero: !0
                    },
                    shadowSize: 3,
                    highlightColor: null
                },
                grid: {
                    show: !0,
                    aboveData: !1,
                    color: "#545454",
                    backgroundColor: null,
                    borderColor: null,
                    tickColor: null,
                    margin: 0,
                    labelMargin: 5,
                    axisMargin: 8,
                    borderWidth: 2,
                    minBorderMargin: null,
                    markings: null,
                    markingsColor: "#f4f4f4",
                    markingsLineWidth: 2,
                    clickable: !1,
                    hoverable: !1,
                    autoHighlight: !0,
                    mouseActiveRadius: 10
                },
                interaction: {
                    redrawOverlayInterval: 1e3 / 60
                },
                hooks: {}
            },
            f = null,
            l = null,
            c = null,
            h = null,
            p = null,
            d = [],
            v = [],
            m = {
                left: 0,
                right: 0,
                top: 0,
                bottom: 0
            },
            g = 0,
            y = 0,
            b = {
                processOptions: [],
                processRawData: [],
                processDatapoints: [],
                processOffset: [],
                drawBackground: [],
                drawSeries: [],
                draw: [],
                bindEvents: [],
                drawOverlay: [],
                shutdown: []
            },
            w = this;
        w.setData = T, w.setupGrid = R, w.draw = V, w.getPlaceholder = function() {
            return t
        }, w.getCanvas = function() {
            return f.element
        }, w.getPlotOffset = function() {
            return m
        }, w.width = function() {
            return g
        }, w.height = function() {
            return y
        }, w.offset = function() {
            var e = c.offset();
            return e.left += m.left, e.top += m.top, e
        }, w.getData = function() {
            return u
        }, w.getAxes = function() {
            var t = {},
                n;
            return e.each(d.concat(v), function(e, n) {
                n && (t[n.direction + (n.n != 1 ? n.n : "") + "axis"] = n)
            }), t
        }, w.getXAxes = function() {
            return d
        }, w.getYAxes = function() {
            return v
        }, w.c2p = L, w.p2c = A, w.getOptions = function() {
            return a
        }, w.highlight = dt, w.unhighlight = vt, w.triggerRedrawOverlay = ht, w.pointOffset = function(e) {
            return {
                left: parseInt(d[C(e, "x") - 1].p2c(+e.x) + m.left, 10),
                top: parseInt(v[C(e, "y") - 1].p2c(+e.y) + m.top, 10)
            }
        }, w.shutdown = H, w.resize = function() {
            var e = t.width(),
                n = t.height();
            f.resize(e, n), l.resize(e, n)
        }, w.hooks = b, S(w), x(s), D(), T(r), R(), V(), P();
        var st = [],
            ot = null
    }

    function i(e, t) {
        return t * Math.floor(e / t)
    }
    var t = Object.prototype.hasOwnProperty;
    n.prototype.resize = function(e, t) {
        if (e <= 0 || t <= 0) throw new Error("Invalid dimensions for plot, width = " + e + ", height = " + t);
        var n = this.element,
            r = this.context,
            i = this.pixelRatio;
        this.width != e && (n.width = e * i, n.style.width = e + "px", this.width = e), this.height != t && (n.height = t * i, n.style.height = t + "px", this.height = t), r.restore(), r.save(), r.scale(i, i)
    }, n.prototype.clear = function() {
        this.context.clearRect(0, 0, this.width, this.height)
    }, n.prototype.render = function() {
        var e = this._textCache;
        for (var n in e)
            if (t.call(e, n)) {
                var r = this.getTextLayer(n),
                    i = e[n];
                r.hide();
                for (var s in i)
                    if (t.call(i, s)) {
                        var o = i[s];
                        for (var u in o)
                            if (t.call(o, u)) {
                                var a = o[u].positions;
                                for (var f = 0, l; l = a[f]; f++) l.active ? l.rendered || (r.append(l.element), l.rendered = !0) : (a.splice(f--, 1), l.rendered && l.element.detach());
                                a.length == 0 && delete o[u]
                            }
                    }
                r.show()
            }
    }, n.prototype.getTextLayer = function(t) {
        var n = this.text[t];
        return n == null && (this.textContainer == null && (this.textContainer = e("<div class='flot-text'></div>").css({
            position: "absolute",
            top: 0,
            left: 0,
            bottom: 0,
            right: 0,
            "font-size": "smaller",
            color: "#545454"
        }).insertAfter(this.element)), n = this.text[t] = e("<div></div>").addClass(t).css({
            position: "absolute",
            top: 0,
            left: 0,
            bottom: 0,
            right: 0
        }).appendTo(this.textContainer)), n
    }, n.prototype.getTextInfo = function(t, n, r, i, s) {
        var o, u, a, f;
        n = "" + n, typeof r == "object" ? o = r.style + " " + r.variant + " " + r.weight + " " + r.size + "px/" + r.lineHeight + "px " + r.family : o = r, u = this._textCache[t], u == null && (u = this._textCache[t] = {}), a = u[o], a == null && (a = u[o] = {}), f = a[n];
        if (f == null) {
            var l = e("<div></div>").html(n).css({
                position: "absolute",
                "max-width": s,
                top: -9999
            }).appendTo(this.getTextLayer(t));
            typeof r == "object" ? l.css({
                font: o,
                color: r.color
            }) : typeof r == "string" && l.addClass(r), f = a[n] = {
                width: l.outerWidth(!0),
                height: l.outerHeight(!0),
                element: l,
                positions: []
            }, l.detach()
        }
        return f
    }, n.prototype.addText = function(e, t, n, r, i, s, o, u, a) {
        var f = this.getTextInfo(e, r, i, s, o),
            l = f.positions;
        u == "center" ? t -= f.width / 2 : u == "right" && (t -= f.width), a == "middle" ? n -= f.height / 2 : a == "bottom" && (n -= f.height);
        for (var c = 0, h; h = l[c]; c++)
            if (h.x == t && h.y == n) {
                h.active = !0;
                return
            }
        h = {
            active: !0,
            rendered: !1,
            element: l.length ? f.element.clone() : f.element,
            x: t,
            y: n
        }, l.push(h), h.element.css({
            top: Math.round(n),
            left: Math.round(t),
            "text-align": u
        })
    }, n.prototype.removeText = function(e, n, r, i, s, o) {
        if (i == null) {
            var u = this._textCache[e];
            if (u != null)
                for (var a in u)
                    if (t.call(u, a)) {
                        var f = u[a];
                        for (var l in f)
                            if (t.call(f, l)) {
                                var c = f[l].positions;
                                for (var h = 0, p; p = c[h]; h++) p.active = !1
                            }
                    }
        } else {
            var c = this.getTextInfo(e, i, s, o).positions;
            for (var h = 0, p; p = c[h]; h++) p.x == n && p.y == r && (p.active = !1)
        }
    }, e.plot = function(t, n, i) {
        var s = new r(e(t), n, i, e.plot.plugins);
        return s
    }, e.plot.version = "0.8.1", e.plot.plugins = [], e.fn.plot = function(t, n) {
        return this.each(function() {
            e.plot(this, t, n)
        })
    }
}(jQuery);

/*!
 * Flot plugin for rendering pie charts.
 * Copyright (c) 2007-2013 IOLA and Ole Laursen.
 * Licensed under the MIT license.
 * Created by Brian Medendorp
 * Updated with contributions from btburnett3, Anthony Aragues and Xavi Ivars
 */
(function(e) {
    function r(r) {
        function p(t, n, r) {
            l || (l = !0, s = t.getCanvas(), o = e(s).parent(), i = t.getOptions(), t.setData(d(t.getData())))
        }

        function d(t) {
            var n = 0,
                r = 0,
                s = 0,
                o = i.series.pie.combine.color,
                u = [];
            for (var a = 0; a < t.length; ++a) {
                var f = t[a].data;
                e.isArray(f) && f.length == 1 && (f = f[0]), e.isArray(f) ? !isNaN(parseFloat(f[1])) && isFinite(f[1]) ? f[1] = +f[1] : f[1] = 0 : !isNaN(parseFloat(f)) && isFinite(f) ? f = [1, +f] : f = [1, 0], t[a].data = [f]
            }
            for (var a = 0; a < t.length; ++a) n += t[a].data[0][1];
            for (var a = 0; a < t.length; ++a) {
                var f = t[a].data[0][1];
                f / n <= i.series.pie.combine.threshold && (r += f, s++, o || (o = t[a].color))
            }
            for (var a = 0; a < t.length; ++a) {
                var f = t[a].data[0][1];
                (s < 2 || f / n > i.series.pie.combine.threshold) && u.push({
                    data: [
                        [1, f]
                    ],
                    color: t[a].color,
                    label: t[a].label,
                    angle: f * Math.PI * 2 / n,
                    percent: f / (n / 100)
                })
            }
            return s > 1 && u.push({
                data: [
                    [1, r]
                ],
                color: o,
                label: i.series.pie.combine.label,
                angle: r * Math.PI * 2 / n,
                percent: r / (n / 100)
            }), u
        }

        function v(r, s) {
            function y() {
                c.clearRect(0, 0, h, p), o.children().filter(".pieLabel, .pieLabelBackground").remove()
            }

            function b() {
                var e = i.series.pie.shadow.left,
                    t = i.series.pie.shadow.top,
                    n = 10,
                    r = i.series.pie.shadow.alpha,
                    s = i.series.pie.radius > 1 ? i.series.pie.radius : u * i.series.pie.radius;
                if (s >= h / 2 - e || s * i.series.pie.tilt >= p / 2 - t || s <= n) return;
                c.save(), c.translate(e, t), c.globalAlpha = r, c.fillStyle = "#000", c.translate(a, f), c.scale(1, i.series.pie.tilt);
                for (var o = 1; o <= n; o++) c.beginPath(), c.arc(0, 0, s, 0, Math.PI * 2, !1), c.fill(), s -= o;
                c.restore()
            }

            function w() {
                function l(e, t, i) {
                    if (e <= 0 || isNaN(e)) return;
                    i ? c.fillStyle = t : (c.strokeStyle = t, c.lineJoin = "round"), c.beginPath(), Math.abs(e - Math.PI * 2) > 1e-9 && c.moveTo(0, 0), c.arc(0, 0, n, r, r + e / 2, !1), c.arc(0, 0, n, r + e / 2, r + e, !1), c.closePath(), r += e, i ? c.fill() : c.stroke()
                }

                function d() {
                    function l(t, n, s) {
                        if (t.data[0][1] == 0) return !0;
                        var u = i.legend.labelFormatter,
                            l, c = i.series.pie.label.formatter;
                        u ? l = u(t.label, t) : l = t.label, c && (l = c(l, t));
                        var d = (n + t.angle + n) / 2,
                            v = a + Math.round(Math.cos(d) * r),
                            m = f + Math.round(Math.sin(d) * r) * i.series.pie.tilt,
                            g = "<span class='pieLabel' id='pieLabel" + s + "' style='position:absolute;top:" + m + "px;left:" + v + "px;'>" + l + "</span>";
                        o.append(g);
                        var y = o.children("#pieLabel" + s),
                            b = m - y.height() / 2,
                            w = v - y.width() / 2;
                        y.css("top", b), y.css("left", w);
                        if (0 - b > 0 || 0 - w > 0 || p - (b + y.height()) < 0 || h - (w + y.width()) < 0) return !1;
                        if (i.series.pie.label.background.opacity != 0) {
                            var E = i.series.pie.label.background.color;
                            E == null && (E = t.color);
                            var S = "top:" + b + "px;left:" + w + "px;";
                            e("<div class='pieLabelBackground' style='position:absolute;width:" + y.width() + "px;height:" + y.height() + "px;" + S + "background-color:" + E + ";'></div>").css("opacity", i.series.pie.label.background.opacity).insertBefore(y)
                        }
                        return !0
                    }
                    var n = t,
                        r = i.series.pie.label.radius > 1 ? i.series.pie.label.radius : u * i.series.pie.label.radius;
                    for (var s = 0; s < v.length; ++s) {
                        if (v[s].percent >= i.series.pie.label.threshold * 100 && !l(v[s], n, s)) return !1;
                        n += v[s].angle
                    }
                    return !0
                }
                var t = Math.PI * i.series.pie.startAngle,
                    n = i.series.pie.radius > 1 ? i.series.pie.radius : u * i.series.pie.radius;
                c.save(), c.translate(a, f), c.scale(1, i.series.pie.tilt), c.save();
                var r = t;
                for (var s = 0; s < v.length; ++s) v[s].startAngle = r, l(v[s].angle, v[s].color, !0);
                c.restore();
                if (i.series.pie.stroke.width > 0) {
                    c.save(), c.lineWidth = i.series.pie.stroke.width, r = t;
                    for (var s = 0; s < v.length; ++s) l(v[s].angle, i.series.pie.stroke.color, !1);
                    c.restore()
                }
                return m(c), c.restore(), i.series.pie.label.show ? d() : !0
            }
            if (!o) return;
            var h = r.getPlaceholder().width(),
                p = r.getPlaceholder().height(),
                d = o.children().filter(".legend").children().width() || 0;
            c = s, l = !1, u = Math.min(h, p / i.series.pie.tilt) / 2, f = p / 2 + i.series.pie.offset.top, a = h / 2, i.series.pie.offset.left == "auto" ? i.legend.position.match("w") ? a += d / 2 : a -= d / 2 : a += i.series.pie.offset.left, a < u ? a = u : a > h - u && (a = h - u);
            var v = r.getData(),
                g = 0;
            do g > 0 && (u *= n), g += 1, y(), i.series.pie.tilt <= .8 && b(); while (!w() && g < t);
            g >= t && (y(), o.prepend("<div class='error'>Could not draw pie with labels contained inside canvas</div>")), r.setSeries && r.insertLegend && (r.setSeries(v), r.insertLegend())
        }

        function m(e) {
            if (i.series.pie.innerRadius > 0) {
                e.save();
                var t = i.series.pie.innerRadius > 1 ? i.series.pie.innerRadius : u * i.series.pie.innerRadius;
                e.globalCompositeOperation = "destination-out", e.beginPath(), e.fillStyle = i.series.pie.stroke.color, e.arc(0, 0, t, 0, Math.PI * 2, !1), e.fill(), e.closePath(), e.restore(), e.save(), e.beginPath(), e.strokeStyle = i.series.pie.stroke.color, e.arc(0, 0, t, 0, Math.PI * 2, !1), e.stroke(), e.closePath(), e.restore()
            }
        }

        function g(e, t) {
            for (var n = !1, r = -1, i = e.length, s = i - 1; ++r < i; s = r)(e[r][1] <= t[1] && t[1] < e[s][1] || e[s][1] <= t[1] && t[1] < e[r][1]) && t[0] < (e[s][0] - e[r][0]) * (t[1] - e[r][1]) / (e[s][1] - e[r][1]) + e[r][0] && (n = !n);
            return n
        }

        function y(e, t) {
            var n = r.getData(),
                i = r.getOptions(),
                s = i.series.pie.radius > 1 ? i.series.pie.radius : u * i.series.pie.radius,
                o, l;
            for (var h = 0; h < n.length; ++h) {
                var p = n[h];
                if (p.pie.show) {
                    c.save(), c.beginPath(), c.moveTo(0, 0), c.arc(0, 0, s, p.startAngle, p.startAngle + p.angle / 2, !1), c.arc(0, 0, s, p.startAngle + p.angle / 2, p.startAngle + p.angle, !1), c.closePath(), o = e - a, l = t - f;
                    if (c.isPointInPath) {
                        if (c.isPointInPath(e - a, t - f)) return c.restore(), {
                            datapoint: [p.percent, p.data],
                            dataIndex: 0,
                            series: p,
                            seriesIndex: h
                        }
                    } else {
                        var d = s * Math.cos(p.startAngle),
                            v = s * Math.sin(p.startAngle),
                            m = s * Math.cos(p.startAngle + p.angle / 4),
                            y = s * Math.sin(p.startAngle + p.angle / 4),
                            b = s * Math.cos(p.startAngle + p.angle / 2),
                            w = s * Math.sin(p.startAngle + p.angle / 2),
                            E = s * Math.cos(p.startAngle + p.angle / 1.5),
                            S = s * Math.sin(p.startAngle + p.angle / 1.5),
                            x = s * Math.cos(p.startAngle + p.angle),
                            T = s * Math.sin(p.startAngle + p.angle),
                            N = [
                                [0, 0],
                                [d, v],
                                [m, y],
                                [b, w],
                                [E, S],
                                [x, T]
                            ],
                            C = [o, l];
                        if (g(N, C)) return c.restore(), {
                            datapoint: [p.percent, p.data],
                            dataIndex: 0,
                            series: p,
                            seriesIndex: h
                        }
                    }
                    c.restore()
                }
            }
            return null
        }

        function b(e) {
            E("plothover", e)
        }

        function w(e) {
            E("plotclick", e)
        }

        function E(e, t) {
            var n = r.offset(),
                s = parseInt(t.pageX - n.left),
                u = parseInt(t.pageY - n.top),
                a = y(s, u);
            if (i.grid.autoHighlight)
                for (var f = 0; f < h.length; ++f) {
                    var l = h[f];
                    l.auto == e && (!a || l.series != a.series) && x(l.series)
                }
            a && S(a.series, e);
            var c = {
                pageX: t.pageX,
                pageY: t.pageY
            };
            o.trigger(e, [c, a])
        }

        function S(e, t) {
            var n = T(e);
            n == -1 ? (h.push({
                series: e,
                auto: t
            }), r.triggerRedrawOverlay()) : t || (h[n].auto = !1)
        }

        function x(e) {
            e == null && (h = [], r.triggerRedrawOverlay());
            var t = T(e);
            t != -1 && (h.splice(t, 1), r.triggerRedrawOverlay())
        }

        function T(e) {
            for (var t = 0; t < h.length; ++t) {
                var n = h[t];
                if (n.series == e) return t
            }
            return -1
        }

        function N(e, t) {
            function s(e) {
                if (e.angle <= 0 || isNaN(e.angle)) return;
                t.fillStyle = "rgba(255, 255, 255, " + n.series.pie.highlight.opacity + ")", t.beginPath(), Math.abs(e.angle - Math.PI * 2) > 1e-9 && t.moveTo(0, 0), t.arc(0, 0, r, e.startAngle, e.startAngle + e.angle / 2, !1), t.arc(0, 0, r, e.startAngle + e.angle / 2, e.startAngle + e.angle, !1), t.closePath(), t.fill()
            }
            var n = e.getOptions(),
                r = n.series.pie.radius > 1 ? n.series.pie.radius : u * n.series.pie.radius;
            t.save(), t.translate(a, f), t.scale(1, n.series.pie.tilt);
            for (var i = 0; i < h.length; ++i) s(h[i].series);
            m(t), t.restore()
        }
        var s = null,
            o = null,
            u = null,
            a = null,
            f = null,
            l = !1,
            c = null,
            h = [];
        r.hooks.processOptions.push(function(e, t) {
            t.series.pie.show && (t.grid.show = !1, t.series.pie.label.show == "auto" && (t.legend.show ? t.series.pie.label.show = !1 : t.series.pie.label.show = !0), t.series.pie.radius == "auto" && (t.series.pie.label.show ? t.series.pie.radius = .75 : t.series.pie.radius = 1), t.series.pie.tilt > 1 ? t.series.pie.tilt = 1 : t.series.pie.tilt < 0 && (t.series.pie.tilt = 0))
        }), r.hooks.bindEvents.push(function(e, t) {
            var n = e.getOptions();
            n.series.pie.show && (n.grid.hoverable && t.unbind("mousemove").mousemove(b), n.grid.clickable && t.unbind("click").click(w))
        }), r.hooks.processDatapoints.push(function(e, t, n, r) {
            var i = e.getOptions();
            i.series.pie.show && p(e, t, n, r)
        }), r.hooks.drawOverlay.push(function(e, t) {
            var n = e.getOptions();
            n.series.pie.show && N(e, t)
        }), r.hooks.draw.push(function(e, t) {
            var n = e.getOptions();
            n.series.pie.show && v(e, t)
        })
    }
    var t = 10,
        n = .95,
        i = {
            series: {
                pie: {
                    show: !1,
                    radius: "auto",
                    innerRadius: 0,
                    startAngle: 1.5,
                    tilt: 1,
                    shadow: {
                        left: 5,
                        top: 15,
                        alpha: .02
                    },
                    offset: {
                        top: 0,
                        left: "auto"
                    },
                    stroke: {
                        color: "#fff",
                        width: 1
                    },
                    label: {
                        show: "auto",
                        formatter: function(e, t) {
                            return "<div style='font-size:x-small;text-align:center;padding:2px;color:" + t.color + ";'>" + e + "<br/>" + Math.round(t.percent) + "%</div>"
                        },
                        radius: 1,
                        background: {
                            color: null,
                            opacity: 0
                        },
                        threshold: 0
                    },
                    combine: {
                        threshold: -1,
                        color: null,
                        label: "Other"
                    },
                    highlight: {
                        opacity: .5
                    }
                }
            }
        };
    e.plot.plugins.push({
        init: r,
        options: i,
        name: "pie",
        version: "1.1"
    })
})(jQuery);

/*!
 * Flot plugin for automatically redrawing plots as the placeholder resizes.
 * Copyright (c) 2007-2013 IOLA and Ole Laursen.
 * Licensed under the MIT license.
 * Inline dependency:
 * jQuery resize event - v1.1 - 3/14/2010
 * http://benalman.com/projects/jquery-resize-plugin/
 *
 * Copyright (c) 2010 "Cowboy" Ben Alman
 * Dual licensed under the MIT and GPL licenses.
 * http://benalman.com/about/license/
 */
(function(e, t, n) {
    function c() {
        s = t[o](function() {
            r.each(function() {
                var t = e(this),
                    n = t.width(),
                    r = t.height(),
                    i = e.data(this, a);
                (n !== i.w || r !== i.h) && t.trigger(u, [i.w = n, i.h = r])
            }), c()
        }, i[f])
    }
    var r = e([]),
        i = e.resize = e.extend(e.resize, {}),
        s, o = "setTimeout",
        u = "resize",
        a = u + "-special-event",
        f = "delay",
        l = "throttleWindow";
    i[f] = 250, i[l] = !0, e.event.special[u] = {
        setup: function() {
            if (!i[l] && this[o]) return !1;
            var t = e(this);
            r = r.add(t), e.data(this, a, {
                w: t.width(),
                h: t.height()
            }), r.length === 1 && c()
        },
        teardown: function() {
            if (!i[l] && this[o]) return !1;
            var t = e(this);
            r = r.not(t), t.removeData(a), r.length || clearTimeout(s)
        },
        add: function(t) {
            function s(t, i, s) {
                var o = e(this),
                    u = e.data(this, a);
                u.w = i !== n ? i : o.width(), u.h = s !== n ? s : o.height(), r.apply(this, arguments)
            }
            if (!i[l] && this[o]) return !1;
            var r;
            if (e.isFunction(t)) return r = t, s;
            r = t.handler, t.handler = s
        }
    }
})(jQuery, this),
function(e) {
    function n(e) {
        function t() {
            var t = e.getPlaceholder();
            if (t.width() == 0 || t.height() == 0) return;
            e.resize(), e.setupGrid(), e.draw()
        }

        function n(e, n) {
            e.getPlaceholder().resize(t)
        }

        function r(e, n) {
            e.getPlaceholder().unbind("resize", t)
        }
        e.hooks.bindEvents.push(n), e.hooks.shutdown.push(r)
    }
    var t = {};
    e.plot.plugins.push({
        init: n,
        options: t,
        name: "resize",
        version: "1.0"
    })
}(jQuery);

/*!
 * Flot plugin for stacking data sets rather than overlyaing them.
 * Copyright (c) 2007-2013 IOLA and Ole Laursen.
 * Licensed under the MIT license.
 */
(function(e) {
    function n(e) {
        function t(e, t) {
            var n = null;
            for (var r = 0; r < t.length; ++r) {
                if (e == t[r]) break;
                t[r].stack == e.stack && (n = t[r])
            }
            return n
        }

        function n(e, n, r) {
            if (n.stack == null || n.stack === !1) return;
            var i = t(n, e.getData());
            if (!i) return;
            var s = r.pointsize,
                o = r.points,
                u = i.datapoints.pointsize,
                a = i.datapoints.points,
                f = [],
                l, c, h, p, d, v, m = n.lines.show,
                g = n.bars.horizontal,
                y = s > 2 && (g ? r.format[2].x : r.format[2].y),
                b = m && n.lines.steps,
                w = !0,
                E = g ? 1 : 0,
                S = g ? 0 : 1,
                x = 0,
                T = 0,
                N, C;
            for (;;) {
                if (x >= o.length) break;
                N = f.length;
                if (o[x] == null) {
                    for (C = 0; C < s; ++C) f.push(o[x + C]);
                    x += s
                } else if (T >= a.length) {
                    if (!m)
                        for (C = 0; C < s; ++C) f.push(o[x + C]);
                    x += s
                } else if (a[T] == null) {
                    for (C = 0; C < s; ++C) f.push(null);
                    w = !0, T += u
                } else {
                    l = o[x + E], c = o[x + S], p = a[T + E], d = a[T + S], v = 0;
                    if (l == p) {
                        for (C = 0; C < s; ++C) f.push(o[x + C]);
                        f[N + S] += d, v = d, x += s, T += u
                    } else if (l > p) {
                        if (m && x > 0 && o[x - s] != null) {
                            h = c + (o[x - s + S] - c) * (p - l) / (o[x - s + E] - l), f.push(p), f.push(h + d);
                            for (C = 2; C < s; ++C) f.push(o[x + C]);
                            v = d
                        }
                        T += u
                    } else {
                        if (w && m) {
                            x += s;
                            continue
                        }
                        for (C = 0; C < s; ++C) f.push(o[x + C]);
                        m && T > 0 && a[T - u] != null && (v = d + (a[T - u + S] - d) * (l - p) / (a[T - u + E] - p)), f[N + S] += v, x += s
                    }
                    w = !1, N != f.length && y && (f[N + 2] += v)
                }
                if (b && N != f.length && N > 0 && f[N] != null && f[N] != f[N - s] && f[N + 1] != f[N - s + 1]) {
                    for (C = 0; C < s; ++C) f[N + s + C] = f[N + C];
                    f[N + 1] = f[N - s + 1]
                }
            }
            r.points = f
        }
        e.hooks.processDatapoints.push(n)
    }
    var t = {
        series: {
            stack: null
        }
    };
    e.plot.plugins.push({
        init: n,
        options: t,
        name: "stack",
        version: "1.2"
    })
})(jQuery);

/*!
 * @name            Elastic
 * @descripton      Elastic is jQuery plugin that grow and shrink your textareas automatically
 * @version         1.6.11
 * @requires        jQuery 1.2.6+
 *
 * @author          Jan Jarfalk
 * @author-email    jan.jarfalk@unwrongest.com
 * @author-website  http://www.unwrongest.com
 *
 * @licence         MIT License - http://www.opensource.org/licenses/mit-license.php
 */
(function(e) {
    jQuery.fn.extend({
        elastic: function() {
            var t = ["paddingTop", "paddingRight", "paddingBottom", "paddingLeft", "fontSize", "lineHeight", "fontFamily", "width", "fontWeight", "border-top-width", "border-right-width", "border-bottom-width", "border-left-width", "borderTopStyle", "borderTopColor", "borderRightStyle", "borderRightColor", "borderBottomStyle", "borderBottomColor", "borderLeftStyle", "borderLeftColor"];
            return this.each(function() {
                function f() {
                    var e = Math.floor(parseInt(n.width(), 10));
                    if (r.width() !== e) {
                        r.css({
                            width: e + "px"
                        });
                        c(true)
                    }
                }

                function l(e, t) {
                    var r = Math.floor(parseInt(e, 10));
                    if (n.height() !== r) {
                        n.css({
                            height: r + "px",
                            overflow: t
                        })
                    }
                }

                function c(e) {
                    var t = n.val().replace(/&/g, "&").replace(/ {2}/g, " ").replace(/<|>/g, ">").replace(/\n/g, "<br />");
                    var u = r.html().replace(/<br>/ig, "<br />");
                    if (e || t + " " !== u) {
                        r.html(t + " ");
                        if (Math.abs(r.height() + i - n.height()) > 3) {
                            var a = r.height() + i;
                            if (a >= o) {
                                l(o, "auto")
                            } else if (a <= s) {
                                l(s, "hidden")
                            } else {
                                l(a, "hidden")
                            }
                        }
                    }
                }
                if (this.type !== "textarea") {
                    return false
                }
                var n = jQuery(this),
                    r = jQuery("<div />").css({
                        position: "absolute",
                        display: "none",
                        "word-wrap": "break-word",
                        "white-space": "pre-wrap"
                    }),
                    i = parseInt(n.css("line-height"), 10) || parseInt(n.css("font-size"), "10"),
                    s = parseInt(n.css("height"), 10) || i * 3,
                    o = parseInt(n.css("max-height"), 10) || Number.MAX_VALUE,
                    u = 0;
                if (o < 0) {
                    o = Number.MAX_VALUE
                }
                r.appendTo(n.parent());
                var a = t.length;
                while (a--) {
                    r.css(t[a].toString(), n.css(t[a].toString()))
                }
                n.css({
                    overflow: "hidden"
                });
                n.bind("keyup change cut paste", function() {
                    c()
                });
                e(window).bind("resize", f);
                n.bind("resize", f);
                n.bind("update", c);
                n.bind("blur", function() {
                    if (r.height() < o) {
                        if (r.height() > s) {
                            n.height(r.height())
                        } else {
                            n.height(s)
                        }
                    }
                });
                n.bind("input paste", function(e) {
                    setTimeout(c, 250)
                });
                c()
            })
        }
    })
})(jQuery);

/*!
 * Dropzone.js 3.7.1
 * http://www.dropzonejs.com/
 */
! function() {
    function a(b, c, d) {
        var e = a.resolve(b);
        if (null == e) {
            d = d || b, c = c || "root";
            var f = new Error('Failed to require "' + d + '" from "' + c + '"');
            throw f.path = d, f.parent = c, f.require = !0, f
        }
        var g = a.modules[e];
        return g.exports || (g.exports = {}, g.client = g.component = !0, g.call(this, g.exports, a.relative(e), g)), g.exports
    }
    a.modules = {}, a.aliases = {}, a.resolve = function(b) {
        "/" === b.charAt(0) && (b = b.slice(1));
        for (var c = [b, b + ".js", b + ".json", b + "/index.js", b + "/index.json"], d = 0; d < c.length; d++) {
            var b = c[d];
            if (a.modules.hasOwnProperty(b)) return b;
            if (a.aliases.hasOwnProperty(b)) return a.aliases[b]
        }
    }, a.normalize = function(a, b) {
        var c = [];
        if ("." != b.charAt(0)) return b;
        a = a.split("/"), b = b.split("/");
        for (var d = 0; d < b.length; ++d) ".." == b[d] ? a.pop() : "." != b[d] && "" != b[d] && c.push(b[d]);
        return a.concat(c).join("/")
    }, a.register = function(b, c) {
        a.modules[b] = c
    }, a.alias = function(b, c) {
        if (!a.modules.hasOwnProperty(b)) throw new Error('Failed to alias "' + b + '", it does not exist');
        a.aliases[c] = b
    }, a.relative = function(b) {
        function c(a, b) {
            for (var c = a.length; c--;)
                if (a[c] === b) return c;
            return -1
        }

        function d(c) {
            var e = d.resolve(c);
            return a(e, b, c)
        }
        var e = a.normalize(b, "..");
        return d.resolve = function(d) {
            var f = d.charAt(0);
            if ("/" == f) return d.slice(1);
            if ("." == f) return a.normalize(e, d);
            var g = b.split("/"),
                h = c(g, "deps") + 1;
            return h || (h = 0), d = g.slice(0, h + 1).join("/") + "/deps/" + d
        }, d.exists = function(b) {
            return a.modules.hasOwnProperty(d.resolve(b))
        }, d
    }, a.register("component-emitter/index.js", function(a, b, c) {
        function d(a) {
            return a ? e(a) : void 0
        }

        function e(a) {
            for (var b in d.prototype) a[b] = d.prototype[b];
            return a
        }
        c.exports = d, d.prototype.on = function(a, b) {
            return this._callbacks = this._callbacks || {}, (this._callbacks[a] = this._callbacks[a] || []).push(b), this
        }, d.prototype.once = function(a, b) {
            function c() {
                d.off(a, c), b.apply(this, arguments)
            }
            var d = this;
            return this._callbacks = this._callbacks || {}, b._off = c, this.on(a, c), this
        }, d.prototype.off = d.prototype.removeListener = d.prototype.removeAllListeners = function(a, b) {
            this._callbacks = this._callbacks || {};
            var c = this._callbacks[a];
            if (!c) return this;
            if (1 == arguments.length) return delete this._callbacks[a], this;
            var d = c.indexOf(b._off || b);
            return ~d && c.splice(d, 1), this
        }, d.prototype.emit = function(a) {
            this._callbacks = this._callbacks || {};
            var b = [].slice.call(arguments, 1),
                c = this._callbacks[a];
            if (c) {
                c = c.slice(0);
                for (var d = 0, e = c.length; e > d; ++d) c[d].apply(this, b)
            }
            return this
        }, d.prototype.listeners = function(a) {
            return this._callbacks = this._callbacks || {}, this._callbacks[a] || []
        }, d.prototype.hasListeners = function(a) {
            return !!this.listeners(a).length
        }
    }), a.register("dropzone/index.js", function(a, b, c) {
        c.exports = b("./lib/dropzone.js")
    }), a.register("dropzone/lib/dropzone.js", function(a, b, c) {
        ! function() {
            var a, d, e, f, g, h, i = {}.hasOwnProperty,
                j = function(a, b) {
                    function c() {
                        this.constructor = a
                    }
                    for (var d in b) i.call(b, d) && (a[d] = b[d]);
                    return c.prototype = b.prototype, a.prototype = new c, a.__super__ = b.prototype, a
                },
                k = [].slice;
            d = "undefined" != typeof Emitter && null !== Emitter ? Emitter : b("emitter"), g = function() {}, a = function(a) {
                function b(a, d) {
                    var e, f, g;
                    if (this.element = a, this.version = b.version, this.defaultOptions.previewTemplate = this.defaultOptions.previewTemplate.replace(/\n*/g, ""), this.clickableElements = [], this.listeners = [], this.files = [], "string" == typeof this.element && (this.element = document.querySelector(this.element)), !this.element || null == this.element.nodeType) throw new Error("Invalid dropzone element.");
                    if (this.element.dropzone) throw new Error("Dropzone already attached.");
                    if (b.instances.push(this), a.dropzone = this, e = null != (g = b.optionsForElement(this.element)) ? g : {}, this.options = c({}, this.defaultOptions, e, null != d ? d : {}), this.options.forceFallback || !b.isBrowserSupported()) return this.options.fallback.call(this);
                    if (null == this.options.url && (this.options.url = this.element.getAttribute("action")), !this.options.url) throw new Error("No URL provided.");
                    if (this.options.acceptedFiles && this.options.acceptedMimeTypes) throw new Error("You can't provide both 'acceptedFiles' and 'acceptedMimeTypes'. 'acceptedMimeTypes' is deprecated.");
                    this.options.acceptedMimeTypes && (this.options.acceptedFiles = this.options.acceptedMimeTypes, delete this.options.acceptedMimeTypes), this.options.method = this.options.method.toUpperCase(), (f = this.getExistingFallback()) && f.parentNode && f.parentNode.removeChild(f), this.previewsContainer = this.options.previewsContainer ? b.getElement(this.options.previewsContainer, "previewsContainer") : this.element, this.options.clickable && (this.clickableElements = this.options.clickable === !0 ? [this.element] : b.getElements(this.options.clickable, "clickable")), this.init()
                }
                var c;
                return j(b, a), b.prototype.events = ["drop", "dragstart", "dragend", "dragenter", "dragover", "dragleave", "selectedfiles", "addedfile", "removedfile", "thumbnail", "error", "errormultiple", "processing", "processingmultiple", "uploadprogress", "totaluploadprogress", "sending", "sendingmultiple", "success", "successmultiple", "canceled", "canceledmultiple", "complete", "completemultiple", "reset", "maxfilesexceeded"], b.prototype.defaultOptions = {
                    url: null,
                    method: "post",
                    withCredentials: !1,
                    parallelUploads: 2,
                    uploadMultiple: !1,
                    maxFilesize: 256,
                    paramName: "file",
                    createImageThumbnails: !0,
                    maxThumbnailFilesize: 10,
                    thumbnailWidth: 100,
                    thumbnailHeight: 100,
                    maxFiles: null,
                    params: {},
                    clickable: !0,
                    ignoreHiddenFiles: !0,
                    acceptedFiles: null,
                    acceptedMimeTypes: null,
                    autoProcessQueue: !0,
                    addRemoveLinks: !1,
                    previewsContainer: null,
                    dictDefaultMessage: "Drop files here to upload",
                    dictFallbackMessage: "Your browser does not support drag'n'drop file uploads.",
                    dictFallbackText: "Please use the fallback form below to upload your files like in the olden days.",
                    dictFileTooBig: "File is too big ({{filesize}}MB). Max filesize: {{maxFilesize}}MB.",
                    dictInvalidFileType: "You can't upload files of this type.",
                    dictResponseError: "Server responded with {{statusCode}} code.",
                    dictCancelUpload: "Cancel upload",
                    dictCancelUploadConfirmation: "Are you sure you want to cancel this upload?",
                    dictRemoveFile: "Remove file",
                    dictRemoveFileConfirmation: null,
                    dictMaxFilesExceeded: "You can only upload {{maxFiles}} files.",
                    accept: function(a, b) {
                        return b()
                    },
                    init: function() {
                        return g
                    },
                    forceFallback: !1,
                    fallback: function() {
                        var a, c, d, e, f, g;
                        for (this.element.className = "" + this.element.className + " dz-browser-not-supported", g = this.element.getElementsByTagName("div"), e = 0, f = g.length; f > e; e++) a = g[e], /(^| )dz-message($| )/.test(a.className) && (c = a, a.className = "dz-message");
                        return c || (c = b.createElement('<div class="dz-message"><span></span></div>'), this.element.appendChild(c)), d = c.getElementsByTagName("span")[0], d && (d.textContent = this.options.dictFallbackMessage), this.element.appendChild(this.getFallbackForm())
                    },
                    resize: function(a) {
                        var b, c, d;
                        return b = {
                            srcX: 0,
                            srcY: 0,
                            srcWidth: a.width,
                            srcHeight: a.height
                        }, c = a.width / a.height, d = this.options.thumbnailWidth / this.options.thumbnailHeight, a.height < this.options.thumbnailHeight || a.width < this.options.thumbnailWidth ? (b.trgHeight = b.srcHeight, b.trgWidth = b.srcWidth) : c > d ? (b.srcHeight = a.height, b.srcWidth = b.srcHeight * d) : (b.srcWidth = a.width, b.srcHeight = b.srcWidth / d), b.srcX = (a.width - b.srcWidth) / 2, b.srcY = (a.height - b.srcHeight) / 2, b
                    },
                    drop: function() {
                        return this.element.classList.remove("dz-drag-hover")
                    },
                    dragstart: g,
                    dragend: function() {
                        return this.element.classList.remove("dz-drag-hover")
                    },
                    dragenter: function() {
                        return this.element.classList.add("dz-drag-hover")
                    },
                    dragover: function() {
                        return this.element.classList.add("dz-drag-hover")
                    },
                    dragleave: function() {
                        return this.element.classList.remove("dz-drag-hover")
                    },
                    selectedfiles: function() {
                        return this.element === this.previewsContainer ? this.element.classList.add("dz-started") : void 0
                    },
                    reset: function() {
                        return this.element.classList.remove("dz-started")
                    },
                    addedfile: function(a) {
                        var c = this;
                        return a.previewElement = b.createElement(this.options.previewTemplate), a.previewTemplate = a.previewElement, this.previewsContainer.appendChild(a.previewElement), a.previewElement.querySelector("[data-dz-name]").textContent = a.name, a.previewElement.querySelector("[data-dz-size]").innerHTML = this.filesize(a.size), this.options.addRemoveLinks && (a._removeLink = b.createElement('<a class="dz-remove" href="javascript:undefined;">' + this.options.dictRemoveFile + "</a>"), a._removeLink.addEventListener("click", function(d) {
                            return d.preventDefault(), d.stopPropagation(), a.status === b.UPLOADING ? b.confirm(c.options.dictCancelUploadConfirmation, function() {
                                return c.removeFile(a)
                            }) : c.options.dictRemoveFileConfirmation ? b.confirm(c.options.dictRemoveFileConfirmation, function() {
                                return c.removeFile(a)
                            }) : c.removeFile(a)
                        }), a.previewElement.appendChild(a._removeLink)), this._updateMaxFilesReachedClass()
                    },
                    removedfile: function(a) {
                        var b;
                        return null != (b = a.previewElement) && b.parentNode.removeChild(a.previewElement), this._updateMaxFilesReachedClass()
                    },
                    thumbnail: function(a, b) {
                        var c;
                        return a.previewElement.classList.remove("dz-file-preview"), a.previewElement.classList.add("dz-image-preview"), c = a.previewElement.querySelector("[data-dz-thumbnail]"), c.alt = a.name, c.src = b
                    },
                    error: function(a, b) {
                        return a.previewElement.classList.add("dz-error"), a.previewElement.querySelector("[data-dz-errormessage]").textContent = b
                    },
                    errormultiple: g,
                    processing: function(a) {
                        return a.previewElement.classList.add("dz-processing"), a._removeLink ? a._removeLink.textContent = this.options.dictCancelUpload : void 0
                    },
                    processingmultiple: g,
                    uploadprogress: function(a, b) {
                        return a.previewElement.querySelector("[data-dz-uploadprogress]").style.width = "" + b + "%"
                    },
                    totaluploadprogress: g,
                    sending: g,
                    sendingmultiple: g,
                    success: function(a) {
                        return a.previewElement.classList.add("dz-success")
                    },
                    successmultiple: g,
                    canceled: function(a) {
                        return this.emit("error", a, "Upload canceled.")
                    },
                    canceledmultiple: g,
                    complete: function(a) {
                        return a._removeLink ? a._removeLink.textContent = this.options.dictRemoveFile : void 0
                    },
                    completemultiple: g,
                    maxfilesexceeded: g,
                    previewTemplate: '<div class="dz-preview dz-file-preview">\n <div class="dz-details">\n <div class="dz-filename"><span data-dz-name></span></div>\n <div class="dz-size" data-dz-size></div>\n <img data-dz-thumbnail />\n </div>\n <div class="dz-progress"><span class="dz-upload" data-dz-uploadprogress></span></div>\n <div class="dz-success-mark"><span>✔</span></div>\n <div class="dz-error-mark"><span>✘</span></div>\n <div class="dz-error-message"><span data-dz-errormessage></span></div>\n</div>'
                }, c = function() {
                    var a, b, c, d, e, f, g;
                    for (d = arguments[0], c = 2 <= arguments.length ? k.call(arguments, 1) : [], f = 0, g = c.length; g > f; f++) {
                        b = c[f];
                        for (a in b) e = b[a], d[a] = e
                    }
                    return d
                }, b.prototype.getAcceptedFiles = function() {
                    var a, b, c, d, e;
                    for (d = this.files, e = [], b = 0, c = d.length; c > b; b++) a = d[b], a.accepted && e.push(a);
                    return e
                }, b.prototype.getRejectedFiles = function() {
                    var a, b, c, d, e;
                    for (d = this.files, e = [], b = 0, c = d.length; c > b; b++) a = d[b], a.accepted || e.push(a);
                    return e
                }, b.prototype.getQueuedFiles = function() {
                    var a, c, d, e, f;
                    for (e = this.files, f = [], c = 0, d = e.length; d > c; c++) a = e[c], a.status === b.QUEUED && f.push(a);
                    return f
                }, b.prototype.getUploadingFiles = function() {
                    var a, c, d, e, f;
                    for (e = this.files, f = [], c = 0, d = e.length; d > c; c++) a = e[c], a.status === b.UPLOADING && f.push(a);
                    return f
                }, b.prototype.init = function() {
                    var a, c, d, e, f, g, h, i = this;
                    for ("form" === this.element.tagName && this.element.setAttribute("enctype", "multipart/form-data"), this.element.classList.contains("dropzone") && !this.element.querySelector(".dz-message") && this.element.appendChild(b.createElement('<div class="dz-default dz-message"><span>' + this.options.dictDefaultMessage + "</span></div>")), this.clickableElements.length && (d = function() {
                            return i.hiddenFileInput && document.body.removeChild(i.hiddenFileInput), i.hiddenFileInput = document.createElement("input"), i.hiddenFileInput.setAttribute("type", "file"), i.hiddenFileInput.setAttribute("multiple", "multiple"), null != i.options.acceptedFiles && i.hiddenFileInput.setAttribute("accept", i.options.acceptedFiles), i.hiddenFileInput.style.visibility = "hidden", i.hiddenFileInput.style.position = "absolute", i.hiddenFileInput.style.top = "0", i.hiddenFileInput.style.left = "0", i.hiddenFileInput.style.height = "0", i.hiddenFileInput.style.width = "0", document.body.appendChild(i.hiddenFileInput), i.hiddenFileInput.addEventListener("change", function() {
                                var a;
                                return a = i.hiddenFileInput.files, a.length && (i.emit("selectedfiles", a), i.handleFiles(a)), d()
                            })
                        }, d()), this.URL = null != (g = window.URL) ? g : window.webkitURL, h = this.events, e = 0, f = h.length; f > e; e++) a = h[e], this.on(a, this.options[a]);
                    return this.on("uploadprogress", function() {
                        return i.updateTotalUploadProgress()
                    }), this.on("removedfile", function() {
                        return i.updateTotalUploadProgress()
                    }), this.on("canceled", function(a) {
                        return i.emit("complete", a)
                    }), c = function(a) {
                        return a.stopPropagation(), a.preventDefault ? a.preventDefault() : a.returnValue = !1
                    }, this.listeners = [{
                        element: this.element,
                        events: {
                            dragstart: function(a) {
                                return i.emit("dragstart", a)
                            },
                            dragenter: function(a) {
                                return c(a), i.emit("dragenter", a)
                            },
                            dragover: function(a) {
                                return c(a), i.emit("dragover", a)
                            },
                            dragleave: function(a) {
                                return i.emit("dragleave", a)
                            },
                            drop: function(a) {
                                return c(a), i.drop(a)
                            },
                            dragend: function(a) {
                                return i.emit("dragend", a)
                            }
                        }
                    }], this.clickableElements.forEach(function(a) {
                        return i.listeners.push({
                            element: a,
                            events: {
                                click: function(c) {
                                    return a !== i.element || c.target === i.element || b.elementInside(c.target, i.element.querySelector(".dz-message")) ? i.hiddenFileInput.click() : void 0
                                }
                            }
                        })
                    }), this.enable(), this.options.init.call(this)
                }, b.prototype.destroy = function() {
                    var a;
                    return this.disable(), this.removeAllFiles(!0), (null != (a = this.hiddenFileInput) ? a.parentNode : void 0) && (this.hiddenFileInput.parentNode.removeChild(this.hiddenFileInput), this.hiddenFileInput = null), delete this.element.dropzone
                }, b.prototype.updateTotalUploadProgress = function() {
                    var a, b, c, d, e, f, g, h;
                    if (d = 0, c = 0, a = this.getAcceptedFiles(), a.length) {
                        for (h = this.getAcceptedFiles(), f = 0, g = h.length; g > f; f++) b = h[f], d += b.upload.bytesSent, c += b.upload.total;
                        e = 100 * d / c
                    } else e = 100;
                    return this.emit("totaluploadprogress", e, c, d)
                }, b.prototype.getFallbackForm = function() {
                    var a, c, d, e;
                    return (a = this.getExistingFallback()) ? a : (d = '<div class="dz-fallback">', this.options.dictFallbackText && (d += "<p>" + this.options.dictFallbackText + "</p>"), d += '<input type="file" name="' + this.options.paramName + (this.options.uploadMultiple ? "[]" : "") + '" ' + (this.options.uploadMultiple ? 'multiple="multiple"' : void 0) + ' /><br><button type="submit" class="btn btn-default">Upload!</button></div>', c = b.createElement(d), "FORM" !== this.element.tagName ? (e = b.createElement('<form action="' + this.options.url + '" enctype="multipart/form-data" method="' + this.options.method + '"></form>'), e.appendChild(c)) : (this.element.setAttribute("enctype", "multipart/form-data"), this.element.setAttribute("method", this.options.method)), null != e ? e : c)
                }, b.prototype.getExistingFallback = function() {
                    var a, b, c, d, e, f;
                    for (b = function(a) {
                            var b, c, d;
                            for (c = 0, d = a.length; d > c; c++)
                                if (b = a[c], /(^| )fallback($| )/.test(b.className)) return b
                        }, f = ["div", "form"], d = 0, e = f.length; e > d; d++)
                        if (c = f[d], a = b(this.element.getElementsByTagName(c))) return a
                }, b.prototype.setupEventListeners = function() {
                    var a, b, c, d, e, f, g;
                    for (f = this.listeners, g = [], d = 0, e = f.length; e > d; d++) a = f[d], g.push(function() {
                        var d, e;
                        d = a.events, e = [];
                        for (b in d) c = d[b], e.push(a.element.addEventListener(b, c, !1));
                        return e
                    }());
                    return g
                }, b.prototype.removeEventListeners = function() {
                    var a, b, c, d, e, f, g;
                    for (f = this.listeners, g = [], d = 0, e = f.length; e > d; d++) a = f[d], g.push(function() {
                        var d, e;
                        d = a.events, e = [];
                        for (b in d) c = d[b], e.push(a.element.removeEventListener(b, c, !1));
                        return e
                    }());
                    return g
                }, b.prototype.disable = function() {
                    var a, b, c, d, e;
                    for (this.clickableElements.forEach(function(a) {
                            return a.classList.remove("dz-clickable")
                        }), this.removeEventListeners(), d = this.files, e = [], b = 0, c = d.length; c > b; b++) a = d[b], e.push(this.cancelUpload(a));
                    return e
                }, b.prototype.enable = function() {
                    return this.clickableElements.forEach(function(a) {
                        return a.classList.add("dz-clickable")
                    }), this.setupEventListeners()
                }, b.prototype.filesize = function(a) {
                    var b;
                    return a >= 1e11 ? (a /= 1e11, b = "TB") : a >= 1e8 ? (a /= 1e8, b = "GB") : a >= 1e5 ? (a /= 1e5, b = "MB") : a >= 100 ? (a /= 100, b = "KB") : (a = 10 * a, b = "b"), "<strong>" + Math.round(a) / 10 + "</strong> " + b
                }, b.prototype._updateMaxFilesReachedClass = function() {
                    return this.options.maxFiles && this.getAcceptedFiles().length >= this.options.maxFiles ? this.element.classList.add("dz-max-files-reached") : this.element.classList.remove("dz-max-files-reached")
                }, b.prototype.drop = function(a) {
                    var b, c;
                    a.dataTransfer && (this.emit("drop", a), b = a.dataTransfer.files, this.emit("selectedfiles", b), b.length && (c = a.dataTransfer.items, c && c.length && (null != c[0].webkitGetAsEntry || null != c[0].getAsEntry) ? this.handleItems(c) : this.handleFiles(b)))
                }, b.prototype.handleFiles = function(a) {
                    var b, c, d, e;
                    for (e = [], c = 0, d = a.length; d > c; c++) b = a[c], e.push(this.addFile(b));
                    return e
                }, b.prototype.handleItems = function(a) {
                    var b, c, d, e;
                    for (d = 0, e = a.length; e > d; d++) c = a[d], null != c.webkitGetAsEntry ? (b = c.webkitGetAsEntry(), b.isFile ? this.addFile(c.getAsFile()) : b.isDirectory && this.addDirectory(b, b.name)) : this.addFile(c.getAsFile())
                }, b.prototype.accept = function(a, c) {
                    return a.size > 1024 * 1024 * this.options.maxFilesize ? c(this.options.dictFileTooBig.replace("{{filesize}}", Math.round(a.size / 1024 / 10.24) / 100).replace("{{maxFilesize}}", this.options.maxFilesize)) : b.isValidFile(a, this.options.acceptedFiles) ? this.options.maxFiles && this.getAcceptedFiles().length >= this.options.maxFiles ? (c(this.options.dictMaxFilesExceeded.replace("{{maxFiles}}", this.options.maxFiles)), this.emit("maxfilesexceeded", a)) : this.options.accept.call(this, a, c) : c(this.options.dictInvalidFileType)
                }, b.prototype.addFile = function(a) {
                    var c = this;
                    return a.upload = {
                        progress: 0,
                        total: a.size,
                        bytesSent: 0
                    }, this.files.push(a), a.status = b.ADDED, this.emit("addedfile", a), this.options.createImageThumbnails && a.type.match(/image.*/) && a.size <= 1024 * 1024 * this.options.maxThumbnailFilesize && this.createThumbnail(a), this.accept(a, function(b) {
                        return b ? (a.accepted = !1, c._errorProcessing([a], b)) : c.enqueueFile(a)
                    })
                }, b.prototype.enqueueFiles = function(a) {
                    var b, c, d;
                    for (c = 0, d = a.length; d > c; c++) b = a[c], this.enqueueFile(b);
                    return null
                }, b.prototype.enqueueFile = function(a) {
                    var c = this;
                    if (a.accepted = !0, a.status !== b.ADDED) throw new Error("This file can't be queued because it has already been processed or was rejected.");
                    return a.status = b.QUEUED, this.options.autoProcessQueue ? setTimeout(function() {
                        return c.processQueue()
                    }, 1) : void 0
                }, b.prototype.addDirectory = function(a, b) {
                    var c, d, e = this;
                    return c = a.createReader(), d = function(c) {
                        var d, f;
                        for (d = 0, f = c.length; f > d; d++) a = c[d], a.isFile ? a.file(function(a) {
                            return e.options.ignoreHiddenFiles && "." === a.name.substring(0, 1) ? void 0 : (a.fullPath = "" + b + "/" + a.name, e.addFile(a))
                        }) : a.isDirectory && e.addDirectory(a, "" + b + "/" + a.name)
                    }, c.readEntries(d, function(a) {
                        return "undefined" != typeof console && null !== console ? "function" == typeof console.log ? console.log(a) : void 0 : void 0
                    })
                }, b.prototype.removeFile = function(a) {
                    return a.status === b.UPLOADING && this.cancelUpload(a), this.files = h(this.files, a), this.emit("removedfile", a), 0 === this.files.length ? this.emit("reset") : void 0
                }, b.prototype.removeAllFiles = function(a) {
                    var c, d, e, f;
                    for (null == a && (a = !1), f = this.files.slice(), d = 0, e = f.length; e > d; d++) c = f[d], (c.status !== b.UPLOADING || a) && this.removeFile(c);
                    return null
                }, b.prototype.createThumbnail = function(a) {
                    var b, c = this;
                    return b = new FileReader, b.onload = function() {
                        var d;
                        return d = new Image, d.onload = function() {
                            var b, e, f, g, h, i, j, k;
                            return a.width = d.width, a.height = d.height, f = c.options.resize.call(c, a), null == f.trgWidth && (f.trgWidth = c.options.thumbnailWidth), null == f.trgHeight && (f.trgHeight = c.options.thumbnailHeight), b = document.createElement("canvas"), e = b.getContext("2d"), b.width = f.trgWidth, b.height = f.trgHeight, e.drawImage(d, null != (h = f.srcX) ? h : 0, null != (i = f.srcY) ? i : 0, f.srcWidth, f.srcHeight, null != (j = f.trgX) ? j : 0, null != (k = f.trgY) ? k : 0, f.trgWidth, f.trgHeight), g = b.toDataURL("image/png"), c.emit("thumbnail", a, g)
                        }, d.src = b.result
                    }, b.readAsDataURL(a)
                }, b.prototype.processQueue = function() {
                    var a, b, c, d;
                    if (b = this.options.parallelUploads, c = this.getUploadingFiles().length, a = c, !(c >= b) && (d = this.getQueuedFiles(), d.length > 0)) {
                        if (this.options.uploadMultiple) return this.processFiles(d.slice(0, b - c));
                        for (; b > a;) {
                            if (!d.length) return;
                            this.processFile(d.shift()), a++
                        }
                    }
                }, b.prototype.processFile = function(a) {
                    return this.processFiles([a])
                }, b.prototype.processFiles = function(a) {
                    var c, d, e;
                    for (d = 0, e = a.length; e > d; d++) c = a[d], c.processing = !0, c.status = b.UPLOADING, this.emit("processing", c);
                    return this.options.uploadMultiple && this.emit("processingmultiple", a), this.uploadFiles(a)
                }, b.prototype._getFilesWithXhr = function(a) {
                    var b, c;
                    return c = function() {
                        var c, d, e, f;
                        for (e = this.files, f = [], c = 0, d = e.length; d > c; c++) b = e[c], b.xhr === a && f.push(b);
                        return f
                    }.call(this)
                }, b.prototype.cancelUpload = function(a) {
                    var c, d, e, f, g, h, i;
                    if (a.status === b.UPLOADING) {
                        for (d = this._getFilesWithXhr(a.xhr), e = 0, g = d.length; g > e; e++) c = d[e], c.status = b.CANCELED;
                        for (a.xhr.abort(), f = 0, h = d.length; h > f; f++) c = d[f], this.emit("canceled", c);
                        this.options.uploadMultiple && this.emit("canceledmultiple", d)
                    } else((i = a.status) === b.ADDED || i === b.QUEUED) && (a.status = b.CANCELED, this.emit("canceled", a), this.options.uploadMultiple && this.emit("canceledmultiple", [a]));
                    return this.options.autoProcessQueue ? this.processQueue() : void 0
                }, b.prototype.uploadFile = function(a) {
                    return this.uploadFiles([a])
                }, b.prototype.uploadFiles = function(a) {
                    var d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, A, B, C, D, E = this;
                    for (r = new XMLHttpRequest, s = 0, w = a.length; w > s; s++) d = a[s], d.xhr = r;
                    r.open(this.options.method, this.options.url, !0), r.withCredentials = !!this.options.withCredentials, o = null, f = function() {
                        var b, c, e;
                        for (e = [], b = 0, c = a.length; c > b; b++) d = a[b], e.push(E._errorProcessing(a, o || E.options.dictResponseError.replace("{{statusCode}}", r.status), r));
                        return e
                    }, p = function(b) {
                        var c, e, f, g, h, i, j, k, l;
                        if (null != b)
                            for (e = 100 * b.loaded / b.total, f = 0, i = a.length; i > f; f++) d = a[f], d.upload = {
                                progress: e,
                                total: b.total,
                                bytesSent: b.loaded
                            };
                        else {
                            for (c = !0, e = 100, g = 0, j = a.length; j > g; g++) d = a[g], (100 !== d.upload.progress || d.upload.bytesSent !== d.upload.total) && (c = !1), d.upload.progress = e, d.upload.bytesSent = d.upload.total;
                            if (c) return
                        }
                        for (l = [], h = 0, k = a.length; k > h; h++) d = a[h], l.push(E.emit("uploadprogress", d, e, d.upload.bytesSent));
                        return l
                    }, r.onload = function(c) {
                        var d;
                        if (a[0].status !== b.CANCELED && 4 === r.readyState) {
                            if (o = r.responseText, r.getResponseHeader("content-type") && ~r.getResponseHeader("content-type").indexOf("application/json")) try {
                                o = JSON.parse(o)
                            } catch (e) {
                                c = e, o = "Invalid JSON response from server."
                            }
                            return p(), 200 <= (d = r.status) && 300 > d ? E._finished(a, o, c) : f()
                        }
                    }, r.onerror = function() {
                        return a[0].status !== b.CANCELED ? f() : void 0
                    }, n = null != (A = r.upload) ? A : r, n.onprogress = p, i = {
                        Accept: "application/json",
                        "Cache-Control": "no-cache",
                        "X-Requested-With": "XMLHttpRequest"
                    }, this.options.headers && c(i, this.options.headers);
                    for (g in i) h = i[g], r.setRequestHeader(g, h);
                    if (e = new FormData, this.options.params) {
                        B = this.options.params;
                        for (m in B) q = B[m], e.append(m, q)
                    }
                    for (t = 0, x = a.length; x > t; t++) d = a[t], this.emit("sending", d, r, e);
                    if (this.options.uploadMultiple && this.emit("sendingmultiple", a, r, e), "FORM" === this.element.tagName)
                        for (C = this.element.querySelectorAll("input, textarea, select, button"), u = 0, y = C.length; y > u; u++) j = C[u], k = j.getAttribute("name"), l = j.getAttribute("type"), (!l || "checkbox" !== (D = l.toLowerCase()) && "radio" !== D || j.checked) && e.append(k, j.value);
                    for (v = 0, z = a.length; z > v; v++) d = a[v], e.append("" + this.options.paramName + (this.options.uploadMultiple ? "[]" : ""), d, d.name);
                    return r.send(e)
                }, b.prototype._finished = function(a, c, d) {
                    var e, f, g;
                    for (f = 0, g = a.length; g > f; f++) e = a[f], e.status = b.SUCCESS, this.emit("success", e, c, d), this.emit("complete", e);
                    return this.options.uploadMultiple && (this.emit("successmultiple", a, c, d), this.emit("completemultiple", a)), this.options.autoProcessQueue ? this.processQueue() : void 0
                }, b.prototype._errorProcessing = function(a, c, d) {
                    var e, f, g;
                    for (f = 0, g = a.length; g > f; f++) e = a[f], e.status = b.ERROR, this.emit("error", e, c, d), this.emit("complete", e);
                    return this.options.uploadMultiple && (this.emit("errormultiple", a, c, d), this.emit("completemultiple", a)), this.options.autoProcessQueue ? this.processQueue() : void 0
                }, b
            }(d), a.version = "3.7.1", a.options = {}, a.optionsForElement = function(b) {
                return b.id ? a.options[e(b.id)] : void 0
            }, a.instances = [], a.forElement = function(a) {
                if ("string" == typeof a && (a = document.querySelector(a)), null == (null != a ? a.dropzone : void 0)) throw new Error("No Dropzone found for given element. This is probably because you're trying to access it before Dropzone had the time to initialize. Use the `init` option to setup any additional observers on your Dropzone.");
                return a.dropzone
            }, a.autoDiscover = !0, a.discover = function() {
                var b, c, d, e, f, g;
                for (document.querySelectorAll ? d = document.querySelectorAll(".dropzone") : (d = [], b = function(a) {
                        var b, c, e, f;
                        for (f = [], c = 0, e = a.length; e > c; c++) b = a[c], /(^| )dropzone($| )/.test(b.className) ? f.push(d.push(b)) : f.push(void 0);
                        return f
                    }, b(document.getElementsByTagName("div")), b(document.getElementsByTagName("form"))), g = [], e = 0, f = d.length; f > e; e++) c = d[e], a.optionsForElement(c) !== !1 ? g.push(new a(c)) : g.push(void 0);
                return g
            }, a.blacklistedBrowsers = [/opera.*Macintosh.*version\/12/i], a.isBrowserSupported = function() {
                var b, c, d, e, f;
                if (b = !0, window.File && window.FileReader && window.FileList && window.Blob && window.FormData && document.querySelector)
                    if ("classList" in document.createElement("a"))
                        for (f = a.blacklistedBrowsers, d = 0, e = f.length; e > d; d++) c = f[d], c.test(navigator.userAgent) && (b = !1);
                    else b = !1;
                else b = !1;
                return b
            }, h = function(a, b) {
                var c, d, e, f;
                for (f = [], d = 0, e = a.length; e > d; d++) c = a[d], c !== b && f.push(c);
                return f
            }, e = function(a) {
                return a.replace(/[\-_](\w)/g, function(a) {
                    return a[1].toUpperCase()
                })
            }, a.createElement = function(a) {
                var b;
                return b = document.createElement("div"), b.innerHTML = a, b.childNodes[0]
            }, a.elementInside = function(a, b) {
                if (a === b) return !0;
                for (; a = a.parentNode;)
                    if (a === b) return !0;
                return !1
            }, a.getElement = function(a, b) {
                var c;
                if ("string" == typeof a ? c = document.querySelector(a) : null != a.nodeType && (c = a), null == c) throw new Error("Invalid `" + b + "` option provided. Please provide a CSS selector or a plain HTML element.");
                return c
            }, a.getElements = function(a, b) {
                var c, d, e, f, g, h, i, j;
                if (a instanceof Array) {
                    e = [];
                    try {
                        for (f = 0, h = a.length; h > f; f++) d = a[f], e.push(this.getElement(d, b))
                    } catch (k) {
                        c = k, e = null
                    }
                } else if ("string" == typeof a)
                    for (e = [], j = document.querySelectorAll(a), g = 0, i = j.length; i > g; g++) d = j[g], e.push(d);
                else null != a.nodeType && (e = [a]);
                if (null == e || !e.length) throw new Error("Invalid `" + b + "` option provided. Please provide a CSS selector, a plain HTML element or a list of those.");
                return e
            }, a.confirm = function(a, b, c) {
                return window.confirm(a) ? b() : null != c ? c() : void 0
            }, a.isValidFile = function(a, b) {
                var c, d, e, f, g;
                if (!b) return !0;
                for (b = b.split(","), d = a.type, c = d.replace(/\/.*$/, ""), f = 0, g = b.length; g > f; f++)
                    if (e = b[f], e = e.trim(), "." === e.charAt(0)) {
                        if (-1 !== a.name.indexOf(e, a.name.length - e.length)) return !0
                    } else if (/\/\*$/.test(e)) {
                    if (c === e.replace(/\/.*$/, "")) return !0
                } else if (d === e) return !0;
                return !1
            }, "undefined" != typeof jQuery && null !== jQuery && (jQuery.fn.dropzone = function(b) {
                return this.each(function() {
                    return new a(this, b)
                })
            }), "undefined" != typeof c && null !== c ? c.exports = a : window.Dropzone = a, a.ADDED = "added", a.QUEUED = "queued", a.ACCEPTED = a.QUEUED, a.UPLOADING = "uploading", a.PROCESSING = a.UPLOADING, a.CANCELED = "canceled", a.ERROR = "error", a.SUCCESS = "success", f = function(a, b) {
                var c, d, e, f, g, h, i, j, k;
                if (e = !1, k = !0, d = a.document, j = d.documentElement, c = d.addEventListener ? "addEventListener" : "attachEvent", i = d.addEventListener ? "removeEventListener" : "detachEvent", h = d.addEventListener ? "" : "on", f = function(c) {
                        return "readystatechange" !== c.type || "complete" === d.readyState ? (("load" === c.type ? a : d)[i](h + c.type, f, !1), !e && (e = !0) ? b.call(a, c.type || c) : void 0) : void 0
                    }, g = function() {
                        var a;
                        try {
                            j.doScroll("left")
                        } catch (b) {
                            return a = b, setTimeout(g, 50), void 0
                        }
                        return f("poll")
                    }, "complete" !== d.readyState) {
                    if (d.createEventObject && j.doScroll) {
                        try {
                            k = !a.frameElement
                        } catch (l) {}
                        k && g()
                    }
                    return d[c](h + "DOMContentLoaded", f, !1), d[c](h + "readystatechange", f, !1), a[c](h + "load", f, !1)
                }
            }, a._autoDiscoverFunction = function() {
                return a.autoDiscover ? a.discover() : void 0
            }, f(window, a._autoDiscoverFunction)
        }.call(this)
    }), a.alias("component-emitter/index.js", "dropzone/deps/emitter/index.js"), a.alias("component-emitter/index.js", "emitter/index.js"), "object" == typeof exports ? module.exports = a("dropzone") : "function" == typeof define && define.amd ? define(function() {
        return a("dropzone")
    }) : this.Dropzone = a("dropzone")
}();

/*!
 * =========================================================
 * bootstrap-datepicker.js
 * http://www.eyecon.ro/bootstrap-datepicker
 * =========================================================
 * Copyright 2012 Stefan Petre
 * Improvements by Andrew Rowls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================================================
 */
! function(e) {
    function t() {
        return new Date(Date.UTC.apply(Date, arguments))
    }

    function n(t, n) {
        var i, r = e(t).data(),
            a = {},
            o = new RegExp("^" + n.toLowerCase() + "([A-Z])"),
            n = new RegExp("^" + n.toLowerCase());
        for (var s in r) n.test(s) && (i = s.replace(o, function(e, t) {
            return t.toLowerCase()
        }), a[i] = r[s]);
        return a
    }

    function i(t) {
        var n = {};
        if (c[t] || (t = t.split("-")[0], c[t])) {
            var i = c[t];
            return e.each(d, function(e, t) {
                t in i && (n[t] = i[t])
            }), n
        }
    }
    var r = e(window),
        a = function(t, n) {
            this._process_options(n), this.element = e(t), this.isInline = !1, this.isInput = this.element.is("input"), this.component = this.element.is(".date") ? this.element.find(".input-group-addon, .btn") : !1, this.hasInput = this.component && this.element.find("input").length, this.component && 0 === this.component.length && (this.component = !1), this.picker = e(u.template), this._buildEvents(), this._attachEvents(), this.isInline ? this.picker.addClass("datepicker-inline").appendTo(this.element) : this.picker.addClass("datepicker-dropdown dropdown-menu"), this.o.rtl && (this.picker.addClass("datepicker-rtl"), this.picker.find(".prev i, .next i").toggleClass("icon-arrow-left icon-arrow-right")), this.viewMode = this.o.startView, this.o.calendarWeeks && this.picker.find("tfoot th.today").attr("colspan", function(e, t) {
                return parseInt(t) + 1
            }), this._allow_update = !1, this.setStartDate(this._o.startDate), this.setEndDate(this._o.endDate), this.setDaysOfWeekDisabled(this.o.daysOfWeekDisabled), this.fillDow(), this.fillMonths(), this._allow_update = !0, this.update(), this.showMode(), this.isInline && this.show()
        };
    a.prototype = {
        constructor: a,
        _process_options: function(t) {
            this._o = e.extend({}, this._o, t);
            var n = this.o = e.extend({}, this._o),
                i = n.language;
            switch (c[i] || (i = i.split("-")[0], c[i] || (i = l.language)), n.language = i, n.startView) {
                case 2:
                case "decade":
                    n.startView = 2;
                    break;
                case 1:
                case "year":
                    n.startView = 1;
                    break;
                default:
                    n.startView = 0
            }
            switch (n.minViewMode) {
                case 1:
                case "months":
                    n.minViewMode = 1;
                    break;
                case 2:
                case "years":
                    n.minViewMode = 2;
                    break;
                default:
                    n.minViewMode = 0
            }
            n.startView = Math.max(n.startView, n.minViewMode), n.weekStart %= 7, n.weekEnd = (n.weekStart + 6) % 7;
            var r = u.parseFormat(n.format);
            n.startDate !== -1 / 0 && (n.startDate = n.startDate ? n.startDate instanceof Date ? this._local_to_utc(this._zero_time(n.startDate)) : u.parseDate(n.startDate, r, n.language) : -1 / 0), 1 / 0 !== n.endDate && (n.endDate = n.endDate ? n.endDate instanceof Date ? this._local_to_utc(this._zero_time(n.endDate)) : u.parseDate(n.endDate, r, n.language) : 1 / 0), n.daysOfWeekDisabled = n.daysOfWeekDisabled || [], e.isArray(n.daysOfWeekDisabled) || (n.daysOfWeekDisabled = n.daysOfWeekDisabled.split(/[,\s]*/)), n.daysOfWeekDisabled = e.map(n.daysOfWeekDisabled, function(e) {
                return parseInt(e, 10)
            });
            var a = String(n.orientation).toLowerCase().split(/\s+/g),
                o = n.orientation.toLowerCase();
            if (a = e.grep(a, function(e) {
                    return /^auto|left|right|top|bottom$/.test(e)
                }), n.orientation = {
                    x: "auto",
                    y: "auto"
                }, o && "auto" !== o)
                if (1 === a.length) switch (a[0]) {
                    case "top":
                    case "bottom":
                        n.orientation.y = a[0];
                        break;
                    case "left":
                    case "right":
                        n.orientation.x = a[0]
                } else o = e.grep(a, function(e) {
                    return /^left|right$/.test(e)
                }), n.orientation.x = o[0] || "auto", o = e.grep(a, function(e) {
                    return /^top|bottom$/.test(e)
                }), n.orientation.y = o[0] || "auto";
                else;
        },
        _events: [],
        _secondaryEvents: [],
        _applyEvents: function(e) {
            for (var t, n, i = 0; i < e.length; i++) t = e[i][0], n = e[i][1], t.on(n)
        },
        _unapplyEvents: function(e) {
            for (var t, n, i = 0; i < e.length; i++) t = e[i][0], n = e[i][1], t.off(n)
        },
        _buildEvents: function() {
            this.isInput ? this._events = [
                [this.element, {
                    focus: e.proxy(this.show, this),
                    keyup: e.proxy(this.update, this),
                    keydown: e.proxy(this.keydown, this)
                }]
            ] : this.component && this.hasInput ? this._events = [
                [this.element.find("input"), {
                    focus: e.proxy(this.show, this),
                    keyup: e.proxy(this.update, this),
                    keydown: e.proxy(this.keydown, this)
                }],
                [this.component, {
                    click: e.proxy(this.show, this)
                }]
            ] : this.element.is("div") ? this.isInline = !0 : this._events = [
                [this.element, {
                    click: e.proxy(this.show, this)
                }]
            ], this._secondaryEvents = [
                [this.picker, {
                    click: e.proxy(this.click, this)
                }],
                [e(window), {
                    resize: e.proxy(this.place, this)
                }],
                [e(document), {
                    mousedown: e.proxy(function(e) {
                        this.element.is(e.target) || this.element.find(e.target).length || this.picker.is(e.target) || this.picker.find(e.target).length || this.hide()
                    }, this)
                }]
            ]
        },
        _attachEvents: function() {
            this._detachEvents(), this._applyEvents(this._events)
        },
        _detachEvents: function() {
            this._unapplyEvents(this._events)
        },
        _attachSecondaryEvents: function() {
            this._detachSecondaryEvents(), this._applyEvents(this._secondaryEvents)
        },
        _detachSecondaryEvents: function() {
            this._unapplyEvents(this._secondaryEvents)
        },
        _trigger: function(t, n) {
            var i = n || this.date,
                r = this._utc_to_local(i);
            this.element.trigger({
                type: t,
                date: r,
                format: e.proxy(function(e) {
                    var t = e || this.o.format;
                    return u.formatDate(i, t, this.o.language)
                }, this)
            })
        },
        show: function(e) {
            this.isInline || this.picker.appendTo("body"), this.picker.show(), this.height = this.component ? this.component.outerHeight() : this.element.outerHeight(), this.place(), this._attachSecondaryEvents(), e && e.preventDefault(), this._trigger("show")
        },
        hide: function() {
            this.isInline || this.picker.is(":visible") && (this.picker.hide().detach(), this._detachSecondaryEvents(), this.viewMode = this.o.startView, this.showMode(), this.o.forceParse && (this.isInput && this.element.val() || this.hasInput && this.element.find("input").val()) && this.setValue(), this._trigger("hide"))
        },
        remove: function() {
            this.hide(), this._detachEvents(), this._detachSecondaryEvents(), this.picker.remove(), delete this.element.data().datepicker, this.isInput || delete this.element.data().date
        },
        _utc_to_local: function(e) {
            return new Date(e.getTime() + 6e4 * e.getTimezoneOffset())
        },
        _local_to_utc: function(e) {
            return new Date(e.getTime() - 6e4 * e.getTimezoneOffset())
        },
        _zero_time: function(e) {
            return new Date(e.getFullYear(), e.getMonth(), e.getDate())
        },
        _zero_utc_time: function(e) {
            return new Date(Date.UTC(e.getUTCFullYear(), e.getUTCMonth(), e.getUTCDate()))
        },
        getDate: function() {
            return this._utc_to_local(this.getUTCDate())
        },
        getUTCDate: function() {
            return this.date
        },
        setDate: function(e) {
            this.setUTCDate(this._local_to_utc(e))
        },
        setUTCDate: function(e) {
            this.date = e, this.setValue()
        },
        setValue: function() {
            var e = this.getFormattedDate();
            this.isInput ? this.element.val(e).change() : this.component && this.element.find("input").val(e).change()
        },
        getFormattedDate: function(e) {
            return void 0 === e && (e = this.o.format), u.formatDate(this.date, e, this.o.language)
        },
        setStartDate: function(e) {
            this._process_options({
                startDate: e
            }), this.update(), this.updateNavArrows()
        },
        setEndDate: function(e) {
            this._process_options({
                endDate: e
            }), this.update(), this.updateNavArrows()
        },
        setDaysOfWeekDisabled: function(e) {
            this._process_options({
                daysOfWeekDisabled: e
            }), this.update(), this.updateNavArrows()
        },
        place: function() {
            if (!this.isInline) {
                var t = this.picker.outerWidth(),
                    n = this.picker.outerHeight(),
                    i = 10,
                    a = r.width(),
                    o = r.height(),
                    s = r.scrollTop(),
                    l = parseInt(this.element.parents().filter(function() {
                        return "auto" != e(this).css("z-index")
                    }).first().css("z-index")) + 10,
                    d = this.component ? this.component.parent().offset() : this.element.offset(),
                    c = this.component ? this.component.outerHeight(!0) : this.element.outerHeight(!1),
                    u = this.component ? this.component.outerWidth(!0) : this.element.outerWidth(!1),
                    h = d.left,
                    p = d.top;
                this.picker.removeClass("datepicker-orient-top datepicker-orient-bottom datepicker-orient-right datepicker-orient-left"), "auto" !== this.o.orientation.x ? (this.picker.addClass("datepicker-orient-" + this.o.orientation.x), "right" === this.o.orientation.x && (h -= t - u)) : (this.picker.addClass("datepicker-orient-left"), d.left < 0 ? h -= d.left - i : d.left + t > a && (h = a - t - i));
                var f, g, m = this.o.orientation.y;
                "auto" === m && (f = -s + d.top - n, g = s + o - (d.top + c + n), m = Math.max(f, g) === g ? "top" : "bottom"), this.picker.addClass("datepicker-orient-" + m), "top" === m ? p += c : p -= n + parseInt(this.picker.css("padding-top")), this.picker.css({
                    top: p,
                    left: h,
                    zIndex: l
                })
            }
        },
        _allow_update: !0,
        update: function() {
            if (this._allow_update) {
                var e, t = new Date(this.date),
                    n = !1;
                arguments && arguments.length && ("string" == typeof arguments[0] || arguments[0] instanceof Date) ? (e = arguments[0], e instanceof Date && (e = this._local_to_utc(e)), n = !0) : (e = this.isInput ? this.element.val() : this.element.data("date") || this.element.find("input").val(), delete this.element.data().date), this.date = u.parseDate(e, this.o.format, this.o.language), n ? this.setValue() : e ? t.getTime() !== this.date.getTime() && this._trigger("changeDate") : this._trigger("clearDate"), this.date < this.o.startDate ? (this.viewDate = new Date(this.o.startDate), this.date = new Date(this.o.startDate)) : this.date > this.o.endDate ? (this.viewDate = new Date(this.o.endDate), this.date = new Date(this.o.endDate)) : (this.viewDate = new Date(this.date), this.date = new Date(this.date)), this.fill()
            }
        },
        fillDow: function() {
            var e = this.o.weekStart,
                t = "<tr>";
            if (this.o.calendarWeeks) {
                var n = '<th class="cw">&nbsp;</th>';
                t += n, this.picker.find(".datepicker-days thead tr:first-child").prepend(n)
            }
            for (; e < this.o.weekStart + 7;) t += '<th class="dow">' + c[this.o.language].daysMin[e++ % 7] + "</th>";
            t += "</tr>", this.picker.find(".datepicker-days thead").append(t)
        },
        fillMonths: function() {
            for (var e = "", t = 0; 12 > t;) e += '<span class="month">' + c[this.o.language].monthsShort[t++] + "</span>";
            this.picker.find(".datepicker-months td").html(e)
        },
        setRange: function(t) {
            t && t.length ? this.range = e.map(t, function(e) {
                return e.valueOf()
            }) : delete this.range, this.fill()
        },
        getClassNames: function(t) {
            var n = [],
                i = this.viewDate.getUTCFullYear(),
                r = this.viewDate.getUTCMonth(),
                a = this.date.valueOf(),
                o = new Date;
            return t.getUTCFullYear() < i || t.getUTCFullYear() == i && t.getUTCMonth() < r ? n.push("old") : (t.getUTCFullYear() > i || t.getUTCFullYear() == i && t.getUTCMonth() > r) && n.push("new"), this.o.todayHighlight && t.getUTCFullYear() == o.getFullYear() && t.getUTCMonth() == o.getMonth() && t.getUTCDate() == o.getDate() && n.push("today"), a && t.valueOf() == a && n.push("active"), (t.valueOf() < this.o.startDate || t.valueOf() > this.o.endDate || -1 !== e.inArray(t.getUTCDay(), this.o.daysOfWeekDisabled)) && n.push("disabled"), this.range && (t > this.range[0] && t < this.range[this.range.length - 1] && n.push("range"), -1 != e.inArray(t.valueOf(), this.range) && n.push("selected")), n
        },
        fill: function() {
            var n, i = new Date(this.viewDate),
                r = i.getUTCFullYear(),
                a = i.getUTCMonth(),
                o = this.o.startDate !== -1 / 0 ? this.o.startDate.getUTCFullYear() : -1 / 0,
                s = this.o.startDate !== -1 / 0 ? this.o.startDate.getUTCMonth() : -1 / 0,
                l = 1 / 0 !== this.o.endDate ? this.o.endDate.getUTCFullYear() : 1 / 0,
                d = 1 / 0 !== this.o.endDate ? this.o.endDate.getUTCMonth() : 1 / 0;
            this.date && this.date.valueOf(), this.picker.find(".datepicker-days thead th.datepicker-switch").text(c[this.o.language].months[a] + " " + r), this.picker.find("tfoot th.today").text(c[this.o.language].today).toggle(this.o.todayBtn !== !1), this.picker.find("tfoot th.clear").text(c[this.o.language].clear).toggle(this.o.clearBtn !== !1), this.updateNavArrows(), this.fillMonths();
            var h = t(r, a - 1, 28, 0, 0, 0, 0),
                p = u.getDaysInMonth(h.getUTCFullYear(), h.getUTCMonth());
            h.setUTCDate(p), h.setUTCDate(p - (h.getUTCDay() - this.o.weekStart + 7) % 7);
            var f = new Date(h);
            f.setUTCDate(f.getUTCDate() + 42), f = f.valueOf();
            for (var g, m = []; h.valueOf() < f;) {
                if (h.getUTCDay() == this.o.weekStart && (m.push("<tr>"), this.o.calendarWeeks)) {
                    var v = new Date(+h + 864e5 * ((this.o.weekStart - h.getUTCDay() - 7) % 7)),
                        y = new Date(+v + 864e5 * ((11 - v.getUTCDay()) % 7)),
                        b = new Date(+(b = t(y.getUTCFullYear(), 0, 1)) + 864e5 * ((11 - b.getUTCDay()) % 7)),
                        x = (y - b) / 864e5 / 7 + 1;
                    m.push('<td class="cw">' + x + "</td>")
                }
                if (g = this.getClassNames(h), g.push("day"), this.o.beforeShowDay !== e.noop) {
                    var w = this.o.beforeShowDay(this._utc_to_local(h));
                    void 0 === w ? w = {} : "boolean" == typeof w ? w = {
                        enabled: w
                    } : "string" == typeof w && (w = {
                        classes: w
                    }), w.enabled === !1 && g.push("disabled"), w.classes && (g = g.concat(w.classes.split(/\s+/))), w.tooltip && (n = w.tooltip)
                }
                g = e.unique(g), m.push('<td class="' + g.join(" ") + '"' + (n ? ' title="' + n + '"' : "") + ">" + h.getUTCDate() + "</td>"), h.getUTCDay() == this.o.weekEnd && m.push("</tr>"), h.setUTCDate(h.getUTCDate() + 1)
            }
            this.picker.find(".datepicker-days tbody").empty().append(m.join(""));
            var T = this.date && this.date.getUTCFullYear(),
                C = this.picker.find(".datepicker-months").find("th:eq(1)").text(r).end().find("span").removeClass("active");
            T && T == r && C.eq(this.date.getUTCMonth()).addClass("active"), (o > r || r > l) && C.addClass("disabled"), r == o && C.slice(0, s).addClass("disabled"), r == l && C.slice(d + 1).addClass("disabled"), m = "", r = 10 * parseInt(r / 10, 10);
            var k = this.picker.find(".datepicker-years").find("th:eq(1)").text(r + "-" + (r + 9)).end().find("td");
            r -= 1;
            for (var _ = -1; 11 > _; _++) m += '<span class="year' + (-1 == _ ? " old" : 10 == _ ? " new" : "") + (T == r ? " active" : "") + (o > r || r > l ? " disabled" : "") + '">' + r + "</span>", r += 1;
            k.html(m)
        },
        updateNavArrows: function() {
            if (this._allow_update) {
                var e = new Date(this.viewDate),
                    t = e.getUTCFullYear(),
                    n = e.getUTCMonth();
                switch (this.viewMode) {
                    case 0:
                        this.o.startDate !== -1 / 0 && t <= this.o.startDate.getUTCFullYear() && n <= this.o.startDate.getUTCMonth() ? this.picker.find(".prev").css({
                            visibility: "hidden"
                        }) : this.picker.find(".prev").css({
                            visibility: "visible"
                        }), 1 / 0 !== this.o.endDate && t >= this.o.endDate.getUTCFullYear() && n >= this.o.endDate.getUTCMonth() ? this.picker.find(".next").css({
                            visibility: "hidden"
                        }) : this.picker.find(".next").css({
                            visibility: "visible"
                        });
                        break;
                    case 1:
                    case 2:
                        this.o.startDate !== -1 / 0 && t <= this.o.startDate.getUTCFullYear() ? this.picker.find(".prev").css({
                            visibility: "hidden"
                        }) : this.picker.find(".prev").css({
                            visibility: "visible"
                        }), 1 / 0 !== this.o.endDate && t >= this.o.endDate.getUTCFullYear() ? this.picker.find(".next").css({
                            visibility: "hidden"
                        }) : this.picker.find(".next").css({
                            visibility: "visible"
                        })
                }
            }
        },
        click: function(n) {
            n.preventDefault();
            var i = e(n.target).closest("span, td, th");
            if (1 == i.length) switch (i[0].nodeName.toLowerCase()) {
                case "th":
                    switch (i[0].className) {
                        case "datepicker-switch":
                            this.showMode(1);
                            break;
                        case "prev":
                        case "next":
                            var r = u.modes[this.viewMode].navStep * ("prev" == i[0].className ? -1 : 1);
                            switch (this.viewMode) {
                                case 0:
                                    this.viewDate = this.moveMonth(this.viewDate, r), this._trigger("changeMonth", this.viewDate);
                                    break;
                                case 1:
                                case 2:
                                    this.viewDate = this.moveYear(this.viewDate, r), 1 === this.viewMode && this._trigger("changeYear", this.viewDate)
                            }
                            this.fill();
                            break;
                        case "today":
                            var a = new Date;
                            a = t(a.getFullYear(), a.getMonth(), a.getDate(), 0, 0, 0), this.showMode(-2);
                            var o = "linked" == this.o.todayBtn ? null : "view";
                            this._setDate(a, o);
                            break;
                        case "clear":
                            var s;
                            this.isInput ? s = this.element : this.component && (s = this.element.find("input")), s && s.val("").change(), this._trigger("changeDate"), this.update(), this.o.autoclose && this.hide()
                    }
                    break;
                case "span":
                    if (!i.is(".disabled")) {
                        if (this.viewDate.setUTCDate(1), i.is(".month")) {
                            var l = 1,
                                d = i.parent().find("span").index(i),
                                c = this.viewDate.getUTCFullYear();
                            this.viewDate.setUTCMonth(d), this._trigger("changeMonth", this.viewDate), 1 === this.o.minViewMode && this._setDate(t(c, d, l, 0, 0, 0, 0))
                        } else {
                            var c = parseInt(i.text(), 10) || 0,
                                l = 1,
                                d = 0;
                            this.viewDate.setUTCFullYear(c), this._trigger("changeYear", this.viewDate), 2 === this.o.minViewMode && this._setDate(t(c, d, l, 0, 0, 0, 0))
                        }
                        this.showMode(-1), this.fill()
                    }
                    break;
                case "td":
                    if (i.is(".day") && !i.is(".disabled")) {
                        var l = parseInt(i.text(), 10) || 1,
                            c = this.viewDate.getUTCFullYear(),
                            d = this.viewDate.getUTCMonth();
                        i.is(".old") ? 0 === d ? (d = 11, c -= 1) : d -= 1 : i.is(".new") && (11 == d ? (d = 0, c += 1) : d += 1), this._setDate(t(c, d, l, 0, 0, 0, 0))
                    }
            }
        },
        _setDate: function(e, t) {
            t && "date" != t || (this.date = new Date(e)), t && "view" != t || (this.viewDate = new Date(e)), this.fill(), this.setValue(), this._trigger("changeDate");
            var n;
            this.isInput ? n = this.element : this.component && (n = this.element.find("input")), n && n.change(), !this.o.autoclose || t && "date" != t || this.hide()
        },
        moveMonth: function(e, t) {
            if (!t) return e;
            var n, i, r = new Date(e.valueOf()),
                a = r.getUTCDate(),
                o = r.getUTCMonth(),
                s = Math.abs(t);
            if (t = t > 0 ? 1 : -1, 1 == s) i = -1 == t ? function() {
                return r.getUTCMonth() == o
            } : function() {
                return r.getUTCMonth() != n
            }, n = o + t, r.setUTCMonth(n), (0 > n || n > 11) && (n = (n + 12) % 12);
            else {
                for (var l = 0; s > l; l++) r = this.moveMonth(r, t);
                n = r.getUTCMonth(), r.setUTCDate(a), i = function() {
                    return n != r.getUTCMonth()
                }
            }
            for (; i();) r.setUTCDate(--a), r.setUTCMonth(n);
            return r
        },
        moveYear: function(e, t) {
            return this.moveMonth(e, 12 * t)
        },
        dateWithinRange: function(e) {
            return e >= this.o.startDate && e <= this.o.endDate
        },
        keydown: function(e) {
            if (this.picker.is(":not(:visible)")) return 27 == e.keyCode && this.show(), void 0;
            var t, n, i, r = !1;
            switch (e.keyCode) {
                case 27:
                    this.hide(), e.preventDefault();
                    break;
                case 37:
                case 39:
                    if (!this.o.keyboardNavigation) break;
                    t = 37 == e.keyCode ? -1 : 1, e.ctrlKey ? (n = this.moveYear(this.date, t), i = this.moveYear(this.viewDate, t), this._trigger("changeYear", this.viewDate)) : e.shiftKey ? (n = this.moveMonth(this.date, t), i = this.moveMonth(this.viewDate, t), this._trigger("changeMonth", this.viewDate)) : (n = new Date(this.date), n.setUTCDate(this.date.getUTCDate() + t), i = new Date(this.viewDate), i.setUTCDate(this.viewDate.getUTCDate() + t)), this.dateWithinRange(n) && (this.date = n, this.viewDate = i, this.setValue(), this.update(), e.preventDefault(), r = !0);
                    break;
                case 38:
                case 40:
                    if (!this.o.keyboardNavigation) break;
                    t = 38 == e.keyCode ? -1 : 1, e.ctrlKey ? (n = this.moveYear(this.date, t), i = this.moveYear(this.viewDate, t), this._trigger("changeYear", this.viewDate)) : e.shiftKey ? (n = this.moveMonth(this.date, t), i = this.moveMonth(this.viewDate, t), this._trigger("changeMonth", this.viewDate)) : (n = new Date(this.date), n.setUTCDate(this.date.getUTCDate() + 7 * t), i = new Date(this.viewDate), i.setUTCDate(this.viewDate.getUTCDate() + 7 * t)), this.dateWithinRange(n) && (this.date = n, this.viewDate = i, this.setValue(), this.update(), e.preventDefault(), r = !0);
                    break;
                case 13:
                    this.hide(), e.preventDefault();
                    break;
                case 9:
                    this.hide()
            }
            if (r) {
                this._trigger("changeDate");
                var a;
                this.isInput ? a = this.element : this.component && (a = this.element.find("input")), a && a.change()
            }
        },
        showMode: function(e) {
            e && (this.viewMode = Math.max(this.o.minViewMode, Math.min(2, this.viewMode + e))), this.picker.find(">div").hide().filter(".datepicker-" + u.modes[this.viewMode].clsName).css("display", "block"), this.updateNavArrows()
        }
    };
    var o = function(t, n) {
        this.element = e(t), this.inputs = e.map(n.inputs, function(e) {
            return e.jquery ? e[0] : e
        }), delete n.inputs, e(this.inputs).datepicker(n).bind("changeDate", e.proxy(this.dateUpdated, this)), this.pickers = e.map(this.inputs, function(t) {
            return e(t).data("datepicker")
        }), this.updateDates()
    };
    o.prototype = {
        updateDates: function() {
            this.dates = e.map(this.pickers, function(e) {
                return e.date
            }), this.updateRanges()
        },
        updateRanges: function() {
            var t = e.map(this.dates, function(e) {
                return e.valueOf()
            });
            e.each(this.pickers, function(e, n) {
                n.setRange(t)
            })
        },
        dateUpdated: function(t) {
            var n = e(t.target).data("datepicker"),
                i = n.getUTCDate(),
                r = e.inArray(t.target, this.inputs),
                a = this.inputs.length;
            if (-1 != r) {
                if (i < this.dates[r])
                    for (; r >= 0 && i < this.dates[r];) this.pickers[r--].setUTCDate(i);
                else if (i > this.dates[r])
                    for (; a > r && i > this.dates[r];) this.pickers[r++].setUTCDate(i);
                this.updateDates()
            }
        },
        remove: function() {
            e.map(this.pickers, function(e) {
                e.remove()
            }), delete this.element.data().datepicker
        }
    };
    var s = e.fn.datepicker;
    e.fn.datepicker = function(t) {
        var r = Array.apply(null, arguments);
        r.shift();
        var s;
        return this.each(function() {
            var d = e(this),
                c = d.data("datepicker"),
                u = "object" == typeof t && t;
            if (!c) {
                var h = n(this, "date"),
                    p = e.extend({}, l, h, u),
                    f = i(p.language),
                    g = e.extend({}, l, f, h, u);
                if (d.is(".input-daterange") || g.inputs) {
                    var m = {
                        inputs: g.inputs || d.find("input").toArray()
                    };
                    d.data("datepicker", c = new o(this, e.extend(g, m)))
                } else d.data("datepicker", c = new a(this, g))
            }
            return "string" == typeof t && "function" == typeof c[t] && (s = c[t].apply(c, r), void 0 !== s) ? !1 : void 0
        }), void 0 !== s ? s : this
    };
    var l = e.fn.datepicker.defaults = {
            autoclose: !1,
            beforeShowDay: e.noop,
            calendarWeeks: !1,
            clearBtn: !1,
            daysOfWeekDisabled: [],
            endDate: 1 / 0,
            forceParse: !0,
            format: "mm/dd/yyyy",
            keyboardNavigation: !0,
            language: "en",
            minViewMode: 0,
            orientation: "auto",
            rtl: !1,
            startDate: -1 / 0,
            startView: 0,
            todayBtn: !1,
            todayHighlight: !1,
            weekStart: 0
        },
        d = e.fn.datepicker.locale_opts = ["format", "rtl", "weekStart"];
    e.fn.datepicker.Constructor = a;
    var c = e.fn.datepicker.dates = {
            en: {
                days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
                daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
                daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"],
                months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                today: "Today",
                clear: "Clear"
            }
        },
        u = {
            modes: [{
                clsName: "days",
                navFnc: "Month",
                navStep: 1
            }, {
                clsName: "months",
                navFnc: "FullYear",
                navStep: 1
            }, {
                clsName: "years",
                navFnc: "FullYear",
                navStep: 10
            }],
            isLeapYear: function(e) {
                return 0 === e % 4 && 0 !== e % 100 || 0 === e % 400
            },
            getDaysInMonth: function(e, t) {
                return [31, u.isLeapYear(e) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][t]
            },
            validParts: /dd?|DD?|mm?|MM?|yy(?:yy)?/g,
            nonpunctuation: /[^ -\/:-@\[\u3400-\u9fff-`{-~\t\n\r]+/g,
            parseFormat: function(e) {
                var t = e.replace(this.validParts, "\0").split("\0"),
                    n = e.match(this.validParts);
                if (!t || !t.length || !n || 0 === n.length) throw new Error("Invalid date format.");
                return {
                    separators: t,
                    parts: n
                }
            },
            parseDate: function(n, i, r) {
                if (n instanceof Date) return n;
                if ("string" == typeof i && (i = u.parseFormat(i)), /^[\-+]\d+[dmwy]([\s,]+[\-+]\d+[dmwy])*$/.test(n)) {
                    var o, s, l = /([\-+]\d+)([dmwy])/,
                        d = n.match(/([\-+]\d+)([dmwy])/g);
                    n = new Date;
                    for (var h = 0; h < d.length; h++) switch (o = l.exec(d[h]), s = parseInt(o[1]), o[2]) {
                        case "d":
                            n.setUTCDate(n.getUTCDate() + s);
                            break;
                        case "m":
                            n = a.prototype.moveMonth.call(a.prototype, n, s);
                            break;
                        case "w":
                            n.setUTCDate(n.getUTCDate() + 7 * s);
                            break;
                        case "y":
                            n = a.prototype.moveYear.call(a.prototype, n, s)
                    }
                    return t(n.getUTCFullYear(), n.getUTCMonth(), n.getUTCDate(), 0, 0, 0)
                }
                var p, f, o, d = n && n.match(this.nonpunctuation) || [],
                    n = new Date,
                    g = {},
                    m = ["yyyy", "yy", "M", "MM", "m", "mm", "d", "dd"],
                    v = {
                        yyyy: function(e, t) {
                            return e.setUTCFullYear(t)
                        },
                        yy: function(e, t) {
                            return e.setUTCFullYear(2e3 + t)
                        },
                        m: function(e, t) {
                            if (isNaN(e)) return e;
                            for (t -= 1; 0 > t;) t += 12;
                            for (t %= 12, e.setUTCMonth(t); e.getUTCMonth() != t;) e.setUTCDate(e.getUTCDate() - 1);
                            return e
                        },
                        d: function(e, t) {
                            return e.setUTCDate(t)
                        }
                    };
                v.M = v.MM = v.mm = v.m, v.dd = v.d, n = t(n.getFullYear(), n.getMonth(), n.getDate(), 0, 0, 0);
                var y = i.parts.slice();
                if (d.length != y.length && (y = e(y).filter(function(t, n) {
                        return -1 !== e.inArray(n, m)
                    }).toArray()), d.length == y.length) {
                    for (var h = 0, b = y.length; b > h; h++) {
                        if (p = parseInt(d[h], 10), o = y[h], isNaN(p)) switch (o) {
                            case "MM":
                                f = e(c[r].months).filter(function() {
                                    var e = this.slice(0, d[h].length),
                                        t = d[h].slice(0, e.length);
                                    return e == t
                                }), p = e.inArray(f[0], c[r].months) + 1;
                                break;
                            case "M":
                                f = e(c[r].monthsShort).filter(function() {
                                    var e = this.slice(0, d[h].length),
                                        t = d[h].slice(0, e.length);
                                    return e == t
                                }), p = e.inArray(f[0], c[r].monthsShort) + 1
                        }
                        g[o] = p
                    }
                    for (var x, w, h = 0; h < m.length; h++) w = m[h], w in g && !isNaN(g[w]) && (x = new Date(n), v[w](x, g[w]), isNaN(x) || (n = x))
                }
                return n
            },
            formatDate: function(t, n, i) {
                "string" == typeof n && (n = u.parseFormat(n));
                var r = {
                    d: t.getUTCDate(),
                    D: c[i].daysShort[t.getUTCDay()],
                    DD: c[i].days[t.getUTCDay()],
                    m: t.getUTCMonth() + 1,
                    M: c[i].monthsShort[t.getUTCMonth()],
                    MM: c[i].months[t.getUTCMonth()],
                    yy: t.getUTCFullYear().toString().substring(2),
                    yyyy: t.getUTCFullYear()
                };
                r.dd = (r.d < 10 ? "0" : "") + r.d, r.mm = (r.m < 10 ? "0" : "") + r.m;
                for (var t = [], a = e.extend([], n.separators), o = 0, s = n.parts.length; s >= o; o++) a.length && t.push(a.shift()), t.push(r[n.parts[o]]);
                return t.join("")
            },
            headTemplate: '<thead><tr><th class="prev">&laquo;</th><th colspan="5" class="datepicker-switch"></th><th class="next">&raquo;</th></tr></thead>',
            contTemplate: '<tbody><tr><td colspan="7"></td></tr></tbody>',
            footTemplate: '<tfoot><tr><th colspan="7" class="today"></th></tr><tr><th colspan="7" class="clear"></th></tr></tfoot>'
        };
    u.template = '<div class="datepicker"><div class="datepicker-days"><table class=" table-condensed">' + u.headTemplate + "<tbody></tbody>" + u.footTemplate + "</table>" + "</div>" + '<div class="datepicker-months">' + '<table class="table-condensed">' + u.headTemplate + u.contTemplate + u.footTemplate + "</table>" + "</div>" + '<div class="datepicker-years">' + '<table class="table-condensed">' + u.headTemplate + u.contTemplate + u.footTemplate + "</table>" + "</div>" + "</div>", e.fn.datepicker.DPGlobal = u, e.fn.datepicker.noConflict = function() {
        return e.fn.datepicker = s, this
    }, e(document).on("focus.datepicker.data-api click.datepicker.data-api", '[data-provide="datepicker"]', function(t) {
        var n = e(this);
        n.data("datepicker") || (t.preventDefault(), n.datepicker("show"))
    }), e(function() {
        e('[data-provide="datepicker-inline"]').datepicker()
    })
}(window.jQuery);

/*! bootstrap-timepicker v0.2.5
 * http://jdewit.github.com/bootstrap-timepicker
 * Copyright (c) 2013 Joris de Wit
 * MIT License
 */
! function(a, b, c) {
    "use strict";
    var d = function(b, c) {
        this.widget = "", this.$element = a(b), this.defaultTime = c.defaultTime, this.disableFocus = c.disableFocus, this.disableMousewheel = c.disableMousewheel, this.isOpen = c.isOpen, this.minuteStep = c.minuteStep, this.modalBackdrop = c.modalBackdrop, this.secondStep = c.secondStep, this.showInputs = c.showInputs, this.showMeridian = c.showMeridian, this.showSeconds = c.showSeconds, this.template = c.template, this.appendWidgetTo = c.appendWidgetTo, this.showWidgetOnAddonClick = c.showWidgetOnAddonClick, this._init()
    };
    d.prototype = {
        constructor: d,
        _init: function() {
            var b = this;
            this.showWidgetOnAddonClick && (this.$element.parent().hasClass("input-group") || this.$element.parent().hasClass("input-group")) ? (this.$element.parent(".input-group").find(".input-group-addon, .input-group-btn").on({
                "click.timepicker": a.proxy(this.showWidget, this)
            }), this.$element.on({
                "focus.timepicker": a.proxy(this.highlightUnit, this),
                "click.timepicker": a.proxy(this.highlightUnit, this),
                "keydown.timepicker": a.proxy(this.elementKeydown, this),
                "blur.timepicker": a.proxy(this.blurElement, this),
                "mousewheel.timepicker DOMMouseScroll.timepicker": a.proxy(this.mousewheel, this)
            })) : this.template ? this.$element.on({
                "focus.timepicker": a.proxy(this.showWidget, this),
                "click.timepicker": a.proxy(this.showWidget, this),
                "blur.timepicker": a.proxy(this.blurElement, this),
                "mousewheel.timepicker DOMMouseScroll.timepicker": a.proxy(this.mousewheel, this)
            }) : this.$element.on({
                "focus.timepicker": a.proxy(this.highlightUnit, this),
                "click.timepicker": a.proxy(this.highlightUnit, this),
                "keydown.timepicker": a.proxy(this.elementKeydown, this),
                "blur.timepicker": a.proxy(this.blurElement, this),
                "mousewheel.timepicker DOMMouseScroll.timepicker": a.proxy(this.mousewheel, this)
            }), this.$widget = this.template !== !1 ? a(this.getTemplate()).prependTo(this.$element.parents(this.appendWidgetTo)).on("click", a.proxy(this.widgetClick, this)) : !1, this.showInputs && this.$widget !== !1 && this.$widget.find("input").each(function() {
                a(this).on({
                    "click.timepicker": function() {
                        a(this).select()
                    },
                    "keydown.timepicker": a.proxy(b.widgetKeydown, b),
                    "keyup.timepicker": a.proxy(b.widgetKeyup, b)
                })
            }), this.setDefaultTime(this.defaultTime)
        },
        blurElement: function() {
            this.highlightedUnit = null, this.updateFromElementVal()
        },
        clear: function() {
            this.hour = "", this.minute = "", this.second = "", this.meridian = "", this.$element.val("")
        },
        decrementHour: function() {
            if (this.showMeridian)
                if (1 === this.hour) this.hour = 12;
                else {
                    if (12 === this.hour) return this.hour--, this.toggleMeridian();
                    if (0 === this.hour) return this.hour = 11, this.toggleMeridian();
                    this.hour--
                }
            else this.hour <= 0 ? this.hour = 23 : this.hour--
        },
        decrementMinute: function(a) {
            var b;
            b = a ? this.minute - a : this.minute - this.minuteStep, 0 > b ? (this.decrementHour(), this.minute = b + 60) : this.minute = b
        },
        decrementSecond: function() {
            var a = this.second - this.secondStep;
            0 > a ? (this.decrementMinute(!0), this.second = a + 60) : this.second = a
        },
        elementKeydown: function(a) {
            switch (a.keyCode) {
                case 9:
                case 27:
                    this.updateFromElementVal();
                    break;
                case 37:
                    a.preventDefault(), this.highlightPrevUnit();
                    break;
                case 38:
                    switch (a.preventDefault(), this.highlightedUnit) {
                        case "hour":
                            this.incrementHour(), this.highlightHour();
                            break;
                        case "minute":
                            this.incrementMinute(), this.highlightMinute();
                            break;
                        case "second":
                            this.incrementSecond(), this.highlightSecond();
                            break;
                        case "meridian":
                            this.toggleMeridian(), this.highlightMeridian()
                    }
                    this.update();
                    break;
                case 39:
                    a.preventDefault(), this.highlightNextUnit();
                    break;
                case 40:
                    switch (a.preventDefault(), this.highlightedUnit) {
                        case "hour":
                            this.decrementHour(), this.highlightHour();
                            break;
                        case "minute":
                            this.decrementMinute(), this.highlightMinute();
                            break;
                        case "second":
                            this.decrementSecond(), this.highlightSecond();
                            break;
                        case "meridian":
                            this.toggleMeridian(), this.highlightMeridian()
                    }
                    this.update()
            }
        },
        getCursorPosition: function() {
            var a = this.$element.get(0);
            if ("selectionStart" in a) return a.selectionStart;
            if (c.selection) {
                a.focus();
                var b = c.selection.createRange(),
                    d = c.selection.createRange().text.length;
                return b.moveStart("character", -a.value.length), b.text.length - d
            }
        },
        getTemplate: function() {
            var a, b, c, d, e, f;
            switch (this.showInputs ? (b = '<input type="text" class="form-control bootstrap-timepicker-hour" maxlength="2"/>', c = '<input type="text" class="form-control bootstrap-timepicker-minute" maxlength="2"/>', d = '<input type="text" class="form-control bootstrap-timepicker-second" maxlength="2"/>', e = '<input type="text" class="form-control bootstrap-timepicker-meridian" maxlength="2"/>') : (b = '<span class="bootstrap-timepicker-hour"></span>', c = '<span class="bootstrap-timepicker-minute"></span>', d = '<span class="bootstrap-timepicker-second"></span>', e = '<span class="bootstrap-timepicker-meridian"></span>'), f = '<table><tr><td><a href="#" data-action="incrementHour"><i class="icon-chevron-up"></i></a></td><td class="separator">&nbsp;</td><td><a href="#" data-action="incrementMinute"><i class="icon-chevron-up"></i></a></td>' + (this.showSeconds ? '<td class="separator">&nbsp;</td><td><a href="#" data-action="incrementSecond"><i class="icon-chevron-up"></i></a></td>' : "") + (this.showMeridian ? '<td class="separator">&nbsp;</td><td class="meridian-column"><a href="#" data-action="toggleMeridian"><i class="icon-chevron-up"></i></a></td>' : "") + "</tr>" + "<tr>" + "<td>" + b + "</td> " + '<td class="separator">:</td>' + "<td>" + c + "</td> " + (this.showSeconds ? '<td class="separator">:</td><td>' + d + "</td>" : "") + (this.showMeridian ? '<td class="separator">&nbsp;</td><td>' + e + "</td>" : "") + "</tr>" + "<tr>" + '<td><a href="#" data-action="decrementHour"><i class="icon-chevron-down"></i></a></td>' + '<td class="separator"></td>' + '<td><a href="#" data-action="decrementMinute"><i class="icon-chevron-down"></i></a></td>' + (this.showSeconds ? '<td class="separator">&nbsp;</td><td><a href="#" data-action="decrementSecond"><i class="icon-chevron-down"></i></a></td>' : "") + (this.showMeridian ? '<td class="separator">&nbsp;</td><td><a href="#" data-action="toggleMeridian"><i class="icon-chevron-down"></i></a></td>' : "") + "</tr>" + "</table>", this.template) {
                case "modal":
                    a = '<div class="modal fade in" data-backdrop="' + (this.modalBackdrop ? "true" : "false") + '">' + '<div class="modal-dialog"><div class="modal-content"><div class="modal-header">' + '<a href="#" class="close" data-dismiss="modal">×</a>' + "<h3>Pick Time</h3>" + "</div>" + '<div class="modal-content"><div class="bootstrap-timepicker-widget">' + f + "</div></div>" + '<div class="modal-footer">' + '<a href="#" class="btn btn-primary" data-dismiss="modal">OK</a>' + "</div>" + "</div>" + "</div>" + "</div>";
                    break;
                case "dropdown":
                    a = '<div class="bootstrap-timepicker-widget dropdown-menu">' + f + "</div>"
            }
            return a
        },
        getTime: function() {
            return this.hour || this.minute || this.second ? this.hour + ":" + (1 === this.minute.toString().length ? "0" + this.minute : this.minute) + (this.showSeconds ? ":" + (1 === this.second.toString().length ? "0" + this.second : this.second) : "") + (this.showMeridian ? " " + this.meridian : "") : ""
        },
        hideWidget: function() {
            this.isOpen !== !1 && (this.$element.trigger({
                type: "hide.timepicker",
                time: {
                    value: this.getTime(),
                    hours: this.hour,
                    minutes: this.minute,
                    seconds: this.second,
                    meridian: this.meridian
                }
            }), "modal" === this.template && this.$widget.modal ? this.$widget.modal("hide") : this.$widget.removeClass("open"), a(c).off("mousedown.timepicker, touchend.timepicker"), this.isOpen = !1)
        },
        highlightUnit: function() {
            this.position = this.getCursorPosition(), this.position >= 0 && this.position <= 2 ? this.highlightHour() : this.position >= 3 && this.position <= 5 ? this.highlightMinute() : this.position >= 6 && this.position <= 8 ? this.showSeconds ? this.highlightSecond() : this.highlightMeridian() : this.position >= 9 && this.position <= 11 && this.highlightMeridian()
        },
        highlightNextUnit: function() {
            switch (this.highlightedUnit) {
                case "hour":
                    this.highlightMinute();
                    break;
                case "minute":
                    this.showSeconds ? this.highlightSecond() : this.showMeridian ? this.highlightMeridian() : this.highlightHour();
                    break;
                case "second":
                    this.showMeridian ? this.highlightMeridian() : this.highlightHour();
                    break;
                case "meridian":
                    this.highlightHour()
            }
        },
        highlightPrevUnit: function() {
            switch (this.highlightedUnit) {
                case "hour":
                    this.showMeridian ? this.highlightMeridian() : this.showSeconds ? this.highlightSecond() : this.highlightMinute();
                    break;
                case "minute":
                    this.highlightHour();
                    break;
                case "second":
                    this.highlightMinute();
                    break;
                case "meridian":
                    this.showSeconds ? this.highlightSecond() : this.highlightMinute()
            }
        },
        highlightHour: function() {
            var a = this.$element.get(0),
                b = this;
            this.highlightedUnit = "hour", a.setSelectionRange && setTimeout(function() {
                b.hour < 10 ? a.setSelectionRange(0, 1) : a.setSelectionRange(0, 2)
            }, 0)
        },
        highlightMinute: function() {
            var a = this.$element.get(0),
                b = this;
            this.highlightedUnit = "minute", a.setSelectionRange && setTimeout(function() {
                b.hour < 10 ? a.setSelectionRange(2, 4) : a.setSelectionRange(3, 5)
            }, 0)
        },
        highlightSecond: function() {
            var a = this.$element.get(0),
                b = this;
            this.highlightedUnit = "second", a.setSelectionRange && setTimeout(function() {
                b.hour < 10 ? a.setSelectionRange(5, 7) : a.setSelectionRange(6, 8)
            }, 0)
        },
        highlightMeridian: function() {
            var a = this.$element.get(0),
                b = this;
            this.highlightedUnit = "meridian", a.setSelectionRange && (this.showSeconds ? setTimeout(function() {
                b.hour < 10 ? a.setSelectionRange(8, 10) : a.setSelectionRange(9, 11)
            }, 0) : setTimeout(function() {
                b.hour < 10 ? a.setSelectionRange(5, 7) : a.setSelectionRange(6, 8)
            }, 0))
        },
        incrementHour: function() {
            if (this.showMeridian) {
                if (11 === this.hour) return this.hour++, this.toggleMeridian();
                12 === this.hour && (this.hour = 0)
            }
            return 23 === this.hour ? (this.hour = 0, void 0) : (this.hour++, void 0)
        },
        incrementMinute: function(a) {
            var b;
            b = a ? this.minute + a : this.minute + this.minuteStep - this.minute % this.minuteStep, b > 59 ? (this.incrementHour(), this.minute = b - 60) : this.minute = b
        },
        incrementSecond: function() {
            var a = this.second + this.secondStep - this.second % this.secondStep;
            a > 59 ? (this.incrementMinute(!0), this.second = a - 60) : this.second = a
        },
        mousewheel: function(b) {
            if (!this.disableMousewheel) {
                b.preventDefault(), b.stopPropagation();
                var c = b.originalEvent.wheelDelta || -b.originalEvent.detail,
                    d = null;
                switch ("mousewheel" === b.type ? d = -1 * b.originalEvent.wheelDelta : "DOMMouseScroll" === b.type && (d = 40 * b.originalEvent.detail), d && (b.preventDefault(), a(this).scrollTop(d + a(this).scrollTop())), this.highlightedUnit) {
                    case "minute":
                        c > 0 ? this.incrementMinute() : this.decrementMinute(), this.highlightMinute();
                        break;
                    case "second":
                        c > 0 ? this.incrementSecond() : this.decrementSecond(), this.highlightSecond();
                        break;
                    case "meridian":
                        this.toggleMeridian(), this.highlightMeridian();
                        break;
                    default:
                        c > 0 ? this.incrementHour() : this.decrementHour(), this.highlightHour()
                }
                return !1
            }
        },
        remove: function() {
            a("document").off(".timepicker"), this.$widget && this.$widget.remove(), delete this.$element.data().timepicker
        },
        setDefaultTime: function(a) {
            if (this.$element.val()) this.updateFromElementVal();
            else if ("current" === a) {
                var b = new Date,
                    c = b.getHours(),
                    d = b.getMinutes(),
                    e = b.getSeconds(),
                    f = "AM";
                0 !== e && (e = Math.ceil(b.getSeconds() / this.secondStep) * this.secondStep, 60 === e && (d += 1, e = 0)), 0 !== d && (d = Math.ceil(b.getMinutes() / this.minuteStep) * this.minuteStep, 60 === d && (c += 1, d = 0)), this.showMeridian && (0 === c ? c = 12 : c >= 12 ? (c > 12 && (c -= 12), f = "PM") : f = "AM"), this.hour = c, this.minute = d, this.second = e, this.meridian = f, this.update()
            } else a === !1 ? (this.hour = 0, this.minute = 0, this.second = 0, this.meridian = "AM") : this.setTime(a)
        },
        setTime: function(a, b) {
            if (!a) return this.clear(), void 0;
            var c, d, e, f, g;
            "object" == typeof a && a.getMonth ? (d = a.getHours(), e = a.getMinutes(), f = a.getSeconds(), this.showMeridian && (g = "AM", d > 12 && (g = "PM", d %= 12), 12 === d && (g = "PM"))) : (g = null !== a.match(/p/i) ? "PM" : "AM", a = a.replace(/[^0-9\:]/g, ""), c = a.split(":"), d = c[0] ? c[0].toString() : c.toString(), e = c[1] ? c[1].toString() : "", f = c[2] ? c[2].toString() : "", d.length > 4 && (f = d.substr(4, 2)), d.length > 2 && (e = d.substr(2, 2), d = d.substr(0, 2)), e.length > 2 && (f = e.substr(2, 2), e = e.substr(0, 2)), f.length > 2 && (f = f.substr(2, 2)), d = parseInt(d, 10), e = parseInt(e, 10), f = parseInt(f, 10), isNaN(d) && (d = 0), isNaN(e) && (e = 0), isNaN(f) && (f = 0), this.showMeridian ? 1 > d ? d = 1 : d > 12 && (d = 12) : (d >= 24 ? d = 23 : 0 > d && (d = 0), 13 > d && "PM" === g && (d += 12)), 0 > e ? e = 0 : e >= 60 && (e = 59), this.showSeconds && (isNaN(f) ? f = 0 : 0 > f ? f = 0 : f >= 60 && (f = 59))), this.hour = d, this.minute = e, this.second = f, this.meridian = g, this.update(b)
        },
        showWidget: function() {
            if (!this.isOpen && !this.$element.is(":disabled")) {
                var b = this;
                a(c).on("mousedown.timepicker, touchend.timepicker", function(c) {
                    0 === a(c.target).closest(".bootstrap-timepicker-widget").length && b.hideWidget()
                }), this.$element.trigger({
                    type: "show.timepicker",
                    time: {
                        value: this.getTime(),
                        hours: this.hour,
                        minutes: this.minute,
                        seconds: this.second,
                        meridian: this.meridian
                    }
                }), this.disableFocus && this.$element.blur(), this.hour || (this.defaultTime ? this.setDefaultTime(this.defaultTime) : this.setTime("0:0:0")), "modal" === this.template && this.$widget.modal ? this.$widget.modal("show").on("hidden", a.proxy(this.hideWidget, this)) : this.isOpen === !1 && this.$widget.addClass("open"), this.isOpen = !0
            }
        },
        toggleMeridian: function() {
            this.meridian = "AM" === this.meridian ? "PM" : "AM"
        },
        update: function(a) {
            this.updateElement(), a || this.updateWidget(), this.$element.trigger({
                type: "changeTime.timepicker",
                time: {
                    value: this.getTime(),
                    hours: this.hour,
                    minutes: this.minute,
                    seconds: this.second,
                    meridian: this.meridian
                }
            })
        },
        updateElement: function() {
            this.$element.val(this.getTime()).change()
        },
        updateFromElementVal: function() {
            this.setTime(this.$element.val())
        },
        updateWidget: function() {
            if (this.$widget !== !1) {
                var a = this.hour,
                    b = 1 === this.minute.toString().length ? "0" + this.minute : this.minute,
                    c = 1 === this.second.toString().length ? "0" + this.second : this.second;
                this.showInputs ? (this.$widget.find("input.bootstrap-timepicker-hour").val(a), this.$widget.find("input.bootstrap-timepicker-minute").val(b), this.showSeconds && this.$widget.find("input.bootstrap-timepicker-second").val(c), this.showMeridian && this.$widget.find("input.bootstrap-timepicker-meridian").val(this.meridian)) : (this.$widget.find("span.bootstrap-timepicker-hour").text(a), this.$widget.find("span.bootstrap-timepicker-minute").text(b), this.showSeconds && this.$widget.find("span.bootstrap-timepicker-second").text(c), this.showMeridian && this.$widget.find("span.bootstrap-timepicker-meridian").text(this.meridian))
            }
        },
        updateFromWidgetInputs: function() {
            if (this.$widget !== !1) {
                var a = this.$widget.find("input.bootstrap-timepicker-hour").val() + ":" + this.$widget.find("input.bootstrap-timepicker-minute").val() + (this.showSeconds ? ":" + this.$widget.find("input.bootstrap-timepicker-second").val() : "") + (this.showMeridian ? this.$widget.find("input.bootstrap-timepicker-meridian").val() : "");
                this.setTime(a, !0)
            }
        },
        widgetClick: function(b) {
            b.stopPropagation(), b.preventDefault();
            var c = a(b.target),
                d = c.closest("a").data("action");
            d && this[d](), this.update(), c.is("input") && c.get(0).setSelectionRange(0, 2)
        },
        widgetKeydown: function(b) {
            var c = a(b.target),
                d = c.attr("class").replace("bootstrap-timepicker-", "");
            switch (b.keyCode) {
                case 9:
                    if (this.showMeridian && "meridian" === d || this.showSeconds && "second" === d || !this.showMeridian && !this.showSeconds && "minute" === d) return this.hideWidget();
                    break;
                case 27:
                    this.hideWidget();
                    break;
                case 38:
                    switch (b.preventDefault(), d) {
                        case "hour":
                            this.incrementHour();
                            break;
                        case "minute":
                            this.incrementMinute();
                            break;
                        case "second":
                            this.incrementSecond();
                            break;
                        case "meridian":
                            this.toggleMeridian()
                    }
                    this.setTime(this.getTime()), c.get(0).setSelectionRange(0, 2);
                    break;
                case 40:
                    switch (b.preventDefault(), d) {
                        case "hour":
                            this.decrementHour();
                            break;
                        case "minute":
                            this.decrementMinute();
                            break;
                        case "second":
                            this.decrementSecond();
                            break;
                        case "meridian":
                            this.toggleMeridian()
                    }
                    this.setTime(this.getTime()), c.get(0).setSelectionRange(0, 2)
            }
        },
        widgetKeyup: function(a) {
            (65 === a.keyCode || 77 === a.keyCode || 80 === a.keyCode || 46 === a.keyCode || 8 === a.keyCode || a.keyCode >= 46 && a.keyCode <= 57) && this.updateFromWidgetInputs()
        }
    }, a.fn.timepicker = function(b) {
        var c = Array.apply(null, arguments);
        return c.shift(), this.each(function() {
            var e = a(this),
                f = e.data("timepicker"),
                g = "object" == typeof b && b;
            f || e.data("timepicker", f = new d(this, a.extend({}, a.fn.timepicker.defaults, g, a(this).data()))), "string" == typeof b && f[b].apply(f, c)
        })
    }, a.fn.timepicker.defaults = {
        defaultTime: "current",
        disableFocus: !1,
        disableMousewheel: !1,
        isOpen: !1,
        minuteStep: 15,
        modalBackdrop: !1,
        secondStep: 15,
        showSeconds: !1,
        showInputs: !0,
        showMeridian: !0,
        template: "dropdown",
        appendWidgetTo: ".bootstrap-timepicker",
        showWidgetOnAddonClick: !0
    }, a.fn.timepicker.Constructor = d
}(jQuery, window, document);

/*!
 * Chosen v1.0.0 | (c) 2011-2013 by Harvest | MIT License
 * https://github.com/harvesthq/chosen/blob/master/LICENSE.md
 */
! function() {
    var a, AbstractChosen, Chosen, SelectParser, b, c = {}.hasOwnProperty,
        d = function(a, b) {
            function d() {
                this.constructor = a
            }
            for (var e in b) c.call(b, e) && (a[e] = b[e]);
            return d.prototype = b.prototype, a.prototype = new d, a.__super__ = b.prototype, a
        };
    SelectParser = function() {
        function SelectParser() {
            this.options_index = 0, this.parsed = []
        }
        return SelectParser.prototype.add_node = function(a) {
            return "OPTGROUP" === a.nodeName.toUpperCase() ? this.add_group(a) : this.add_option(a)
        }, SelectParser.prototype.add_group = function(a) {
            var b, c, d, e, f, g;
            for (b = this.parsed.length, this.parsed.push({
                    array_index: b,
                    group: !0,
                    label: this.escapeExpression(a.label),
                    children: 0,
                    disabled: a.disabled
                }), f = a.childNodes, g = [], d = 0, e = f.length; e > d; d++) c = f[d], g.push(this.add_option(c, b, a.disabled));
            return g
        }, SelectParser.prototype.add_option = function(a, b, c) {
            return "OPTION" === a.nodeName.toUpperCase() ? ("" !== a.text ? (null != b && (this.parsed[b].children += 1), this.parsed.push({
                array_index: this.parsed.length,
                options_index: this.options_index,
                value: a.value,
                text: a.text,
                html: a.innerHTML,
                selected: a.selected,
                disabled: c === !0 ? c : a.disabled,
                group_array_index: b,
                classes: a.className,
                style: a.style.cssText
            })) : this.parsed.push({
                array_index: this.parsed.length,
                options_index: this.options_index,
                empty: !0
            }), this.options_index += 1) : void 0
        }, SelectParser.prototype.escapeExpression = function(a) {
            var b, c;
            return null == a || a === !1 ? "" : /[\&\<\>\"\'\`]/.test(a) ? (b = {
                "<": "&lt;",
                ">": "&gt;",
                '"': "&quot;",
                "'": "&#x27;",
                "`": "&#x60;"
            }, c = /&(?!\w+;)|[\<\>\"\'\`]/g, a.replace(c, function(a) {
                return b[a] || "&amp;"
            })) : a
        }, SelectParser
    }(), SelectParser.select_to_array = function(a) {
        var b, c, d, e, f;
        for (c = new SelectParser, f = a.childNodes, d = 0, e = f.length; e > d; d++) b = f[d], c.add_node(b);
        return c.parsed
    }, AbstractChosen = function() {
        function AbstractChosen(a, b) {
            this.form_field = a, this.options = null != b ? b : {}, AbstractChosen.browser_is_supported() && (this.is_multiple = this.form_field.multiple, this.set_default_text(), this.set_default_values(), this.setup(), this.set_up_html(), this.register_observers())
        }
        return AbstractChosen.prototype.set_default_values = function() {
            var a = this;
            return this.click_test_action = function(b) {
                return a.test_active_click(b)
            }, this.activate_action = function(b) {
                return a.activate_field(b)
            }, this.active_field = !1, this.mouse_on_container = !1, this.results_showing = !1, this.result_highlighted = null, this.result_single_selected = null, this.allow_single_deselect = null != this.options.allow_single_deselect && null != this.form_field.options[0] && "" === this.form_field.options[0].text ? this.options.allow_single_deselect : !1, this.disable_search_threshold = this.options.disable_search_threshold || 0, this.disable_search = this.options.disable_search || !1, this.enable_split_word_search = null != this.options.enable_split_word_search ? this.options.enable_split_word_search : !0, this.group_search = null != this.options.group_search ? this.options.group_search : !0, this.search_contains = this.options.search_contains || !1, this.single_backstroke_delete = null != this.options.single_backstroke_delete ? this.options.single_backstroke_delete : !0, this.max_selected_options = this.options.max_selected_options || 1 / 0, this.inherit_select_classes = this.options.inherit_select_classes || !1, this.display_selected_options = null != this.options.display_selected_options ? this.options.display_selected_options : !0, this.display_disabled_options = null != this.options.display_disabled_options ? this.options.display_disabled_options : !0
        }, AbstractChosen.prototype.set_default_text = function() {
            return this.default_text = this.form_field.getAttribute("data-placeholder") ? this.form_field.getAttribute("data-placeholder") : this.is_multiple ? this.options.placeholder_text_multiple || this.options.placeholder_text || AbstractChosen.default_multiple_text : this.options.placeholder_text_single || this.options.placeholder_text || AbstractChosen.default_single_text, this.results_none_found = this.form_field.getAttribute("data-no_results_text") || this.options.no_results_text || AbstractChosen.default_no_result_text
        }, AbstractChosen.prototype.mouse_enter = function() {
            return this.mouse_on_container = !0
        }, AbstractChosen.prototype.mouse_leave = function() {
            return this.mouse_on_container = !1
        }, AbstractChosen.prototype.input_focus = function() {
            var a = this;
            if (this.is_multiple) {
                if (!this.active_field) return setTimeout(function() {
                    return a.container_mousedown()
                }, 50)
            } else if (!this.active_field) return this.activate_field()
        }, AbstractChosen.prototype.input_blur = function() {
            var a = this;
            return this.mouse_on_container ? void 0 : (this.active_field = !1, setTimeout(function() {
                return a.blur_test()
            }, 100))
        }, AbstractChosen.prototype.results_option_build = function(a) {
            var b, c, d, e, f;
            for (b = "", f = this.results_data, d = 0, e = f.length; e > d; d++) c = f[d], b += c.group ? this.result_add_group(c) : this.result_add_option(c), (null != a ? a.first : void 0) && (c.selected && this.is_multiple ? this.choice_build(c) : c.selected && !this.is_multiple && this.single_set_selected_text(c.text));
            return b
        }, AbstractChosen.prototype.result_add_option = function(a) {
            var b, c;
            return a.search_match ? this.include_option_in_results(a) ? (b = [], a.disabled || a.selected && this.is_multiple || b.push("active-result"), !a.disabled || a.selected && this.is_multiple || b.push("disabled-result"), a.selected && b.push("result-selected"), null != a.group_array_index && b.push("group-option"), "" !== a.classes && b.push(a.classes), c = "" !== a.style.cssText ? ' style="' + a.style + '"' : "", '<li class="' + b.join(" ") + '"' + c + ' data-option-array-index="' + a.array_index + '">' + a.search_text + "</li>") : "" : ""
        }, AbstractChosen.prototype.result_add_group = function(a) {
            return a.search_match || a.group_match ? a.active_options > 0 ? '<li class="group-result">' + a.search_text + "</li>" : "" : ""
        }, AbstractChosen.prototype.results_update_field = function() {
            return this.set_default_text(), this.is_multiple || this.results_reset_cleanup(), this.result_clear_highlight(), this.result_single_selected = null, this.results_build(), this.results_showing ? this.winnow_results() : void 0
        }, AbstractChosen.prototype.results_toggle = function() {
            return this.results_showing ? this.results_hide() : this.results_show()
        }, AbstractChosen.prototype.results_search = function() {
            return this.results_showing ? this.winnow_results() : this.results_show()
        }, AbstractChosen.prototype.winnow_results = function() {
            var a, b, c, d, e, f, g, h, i, j, k, l, m;
            for (this.no_results_clear(), e = 0, g = this.get_search_text(), a = g.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&"), d = this.search_contains ? "" : "^", c = new RegExp(d + a, "i"), j = new RegExp(a, "i"), m = this.results_data, k = 0, l = m.length; l > k; k++) b = m[k], b.search_match = !1, f = null, this.include_option_in_results(b) && (b.group && (b.group_match = !1, b.active_options = 0), null != b.group_array_index && this.results_data[b.group_array_index] && (f = this.results_data[b.group_array_index], 0 === f.active_options && f.search_match && (e += 1), f.active_options += 1), (!b.group || this.group_search) && (b.search_text = b.group ? b.label : b.html, b.search_match = this.search_string_match(b.search_text, c), b.search_match && !b.group && (e += 1), b.search_match ? (g.length && (h = b.search_text.search(j), i = b.search_text.substr(0, h + g.length) + "</em>" + b.search_text.substr(h + g.length), b.search_text = i.substr(0, h) + "<em>" + i.substr(h)), null != f && (f.group_match = !0)) : null != b.group_array_index && this.results_data[b.group_array_index].search_match && (b.search_match = !0)));
            return this.result_clear_highlight(), 1 > e && g.length ? (this.update_results_content(""), this.no_results(g)) : (this.update_results_content(this.results_option_build()), this.winnow_results_set_highlight())
        }, AbstractChosen.prototype.search_string_match = function(a, b) {
            var c, d, e, f;
            if (b.test(a)) return !0;
            if (this.enable_split_word_search && (a.indexOf(" ") >= 0 || 0 === a.indexOf("[")) && (d = a.replace(/\[|\]/g, "").split(" "), d.length))
                for (e = 0, f = d.length; f > e; e++)
                    if (c = d[e], b.test(c)) return !0
        }, AbstractChosen.prototype.choices_count = function() {
            var a, b, c, d;
            if (null != this.selected_option_count) return this.selected_option_count;
            for (this.selected_option_count = 0, d = this.form_field.options, b = 0, c = d.length; c > b; b++) a = d[b], a.selected && (this.selected_option_count += 1);
            return this.selected_option_count
        }, AbstractChosen.prototype.choices_click = function(a) {
            return a.preventDefault(), this.results_showing || this.is_disabled ? void 0 : this.results_show()
        }, AbstractChosen.prototype.keyup_checker = function(a) {
            var b, c;
            switch (b = null != (c = a.which) ? c : a.keyCode, this.search_field_scale(), b) {
                case 8:
                    if (this.is_multiple && this.backstroke_length < 1 && this.choices_count() > 0) return this.keydown_backstroke();
                    if (!this.pending_backstroke) return this.result_clear_highlight(), this.results_search();
                    break;
                case 13:
                    if (a.preventDefault(), this.results_showing) return this.result_select(a);
                    break;
                case 27:
                    return this.results_showing && this.results_hide(), !0;
                case 9:
                case 38:
                case 40:
                case 16:
                case 91:
                case 17:
                    break;
                default:
                    return this.results_search()
            }
        }, AbstractChosen.prototype.container_width = function() {
            return null != this.options.width ? this.options.width : "" + this.form_field.offsetWidth + "px"
        }, AbstractChosen.prototype.include_option_in_results = function(a) {
            return this.is_multiple && !this.display_selected_options && a.selected ? !1 : !this.display_disabled_options && a.disabled ? !1 : a.empty ? !1 : !0
        }, AbstractChosen.browser_is_supported = function() {
            return "Microsoft Internet Explorer" === window.navigator.appName ? document.documentMode >= 8 : /iP(od|hone)/i.test(window.navigator.userAgent) ? !1 : /Android/i.test(window.navigator.userAgent) && /Mobile/i.test(window.navigator.userAgent) ? !1 : !0
        }, AbstractChosen.default_multiple_text = "Select Some Options", AbstractChosen.default_single_text = "Select an Option", AbstractChosen.default_no_result_text = "No results match", AbstractChosen
    }(), a = jQuery, a.fn.extend({
        chosen: function(b) {
            return AbstractChosen.browser_is_supported() ? this.each(function() {
                var c, d;
                c = a(this), d = c.data("chosen"), "destroy" === b && d ? d.destroy() : d || c.data("chosen", new Chosen(this, b))
            }) : this
        }
    }), Chosen = function(c) {
        function Chosen() {
            return b = Chosen.__super__.constructor.apply(this, arguments)
        }
        return d(Chosen, c), Chosen.prototype.setup = function() {
            return this.form_field_jq = a(this.form_field), this.current_selectedIndex = this.form_field.selectedIndex, this.is_rtl = this.form_field_jq.hasClass("chosen-rtl")
        }, Chosen.prototype.set_up_html = function() {
            var b, c;
            return b = ["chosen-container"], b.push("chosen-container-" + (this.is_multiple ? "multi" : "single")), this.inherit_select_classes && this.form_field.className && b.push(this.form_field.className), this.is_rtl && b.push("chosen-rtl"), c = {
                "class": b.join(" "),
                style: "width: " + this.container_width() + ";",
                title: this.form_field.title
            }, this.form_field.id.length && (c.id = this.form_field.id.replace(/[^\w]/g, "_") + "_chosen"), this.container = a("<div />", c), this.is_multiple ? this.container.html('<ul class="chosen-choices"><li class="search-field"><input type="text" value="' + this.default_text + '" class="default" autocomplete="off" style="width:25px;" /></li></ul><div class="chosen-drop"><ul class="chosen-results"></ul></div>') : this.container.html('<a class="chosen-single chosen-default" tabindex="-1"><span>' + this.default_text + '</span><div><b></b></div></a><div class="chosen-drop"><div class="chosen-search"><input type="text" autocomplete="off" /></div><ul class="chosen-results"></ul></div>'), this.form_field_jq.hide().after(this.container), this.dropdown = this.container.find("div.chosen-drop").first(), this.search_field = this.container.find("input").first(), this.search_results = this.container.find("ul.chosen-results").first(), this.search_field_scale(), this.search_no_results = this.container.find("li.no-results").first(), this.is_multiple ? (this.search_choices = this.container.find("ul.chosen-choices").first(), this.search_container = this.container.find("li.search-field").first()) : (this.search_container = this.container.find("div.chosen-search").first(), this.selected_item = this.container.find(".chosen-single").first()), this.results_build(), this.set_tab_index(), this.set_label_behavior(), this.form_field_jq.trigger("chosen:ready", {
                chosen: this
            })
        }, Chosen.prototype.register_observers = function() {
            var a = this;
            return this.container.bind("mousedown.chosen", function(b) {
                a.container_mousedown(b)
            }), this.container.bind("mouseup.chosen", function(b) {
                a.container_mouseup(b)
            }), this.container.bind("mouseenter.chosen", function(b) {
                a.mouse_enter(b)
            }), this.container.bind("mouseleave.chosen", function(b) {
                a.mouse_leave(b)
            }), this.search_results.bind("mouseup.chosen", function(b) {
                a.search_results_mouseup(b)
            }), this.search_results.bind("mouseover.chosen", function(b) {
                a.search_results_mouseover(b)
            }), this.search_results.bind("mouseout.chosen", function(b) {
                a.search_results_mouseout(b)
            }), this.search_results.bind("mousewheel.chosen DOMMouseScroll.chosen", function(b) {
                a.search_results_mousewheel(b)
            }), this.form_field_jq.bind("chosen:updated.chosen", function(b) {
                a.results_update_field(b)
            }), this.form_field_jq.bind("chosen:activate.chosen", function(b) {
                a.activate_field(b)
            }), this.form_field_jq.bind("chosen:open.chosen", function(b) {
                a.container_mousedown(b)
            }), this.search_field.bind("blur.chosen", function(b) {
                a.input_blur(b)
            }), this.search_field.bind("keyup.chosen", function(b) {
                a.keyup_checker(b)
            }), this.search_field.bind("keydown.chosen", function(b) {
                a.keydown_checker(b)
            }), this.search_field.bind("focus.chosen", function(b) {
                a.input_focus(b)
            }), this.is_multiple ? this.search_choices.bind("click.chosen", function(b) {
                a.choices_click(b)
            }) : this.container.bind("click.chosen", function(a) {
                a.preventDefault()
            })
        }, Chosen.prototype.destroy = function() {
            return a(document).unbind("click.chosen", this.click_test_action), this.search_field[0].tabIndex && (this.form_field_jq[0].tabIndex = this.search_field[0].tabIndex), this.container.remove(), this.form_field_jq.removeData("chosen"), this.form_field_jq.show()
        }, Chosen.prototype.search_field_disabled = function() {
            return this.is_disabled = this.form_field_jq[0].disabled, this.is_disabled ? (this.container.addClass("chosen-disabled"), this.search_field[0].disabled = !0, this.is_multiple || this.selected_item.unbind("focus.chosen", this.activate_action), this.close_field()) : (this.container.removeClass("chosen-disabled"), this.search_field[0].disabled = !1, this.is_multiple ? void 0 : this.selected_item.bind("focus.chosen", this.activate_action))
        }, Chosen.prototype.container_mousedown = function(b) {
            return this.is_disabled || (b && "mousedown" === b.type && !this.results_showing && b.preventDefault(), null != b && a(b.target).hasClass("search-choice-close")) ? void 0 : (this.active_field ? this.is_multiple || !b || a(b.target)[0] !== this.selected_item[0] && !a(b.target).parents("a.chosen-single").length || (b.preventDefault(), this.results_toggle()) : (this.is_multiple && this.search_field.val(""), a(document).bind("click.chosen", this.click_test_action), this.results_show()), this.activate_field())
        }, Chosen.prototype.container_mouseup = function(a) {
            return "ABBR" !== a.target.nodeName || this.is_disabled ? void 0 : this.results_reset(a)
        }, Chosen.prototype.search_results_mousewheel = function(a) {
            var b, c, d;
            return b = -(null != (c = a.originalEvent) ? c.wheelDelta : void 0) || (null != (d = a.originialEvent) ? d.detail : void 0), null != b ? (a.preventDefault(), "DOMMouseScroll" === a.type && (b = 40 * b), this.search_results.scrollTop(b + this.search_results.scrollTop())) : void 0
        }, Chosen.prototype.blur_test = function() {
            return !this.active_field && this.container.hasClass("chosen-container-active") ? this.close_field() : void 0
        }, Chosen.prototype.close_field = function() {
            return a(document).unbind("click.chosen", this.click_test_action), this.active_field = !1, this.results_hide(), this.container.removeClass("chosen-container-active"), this.clear_backstroke(), this.show_search_field_default(), this.search_field_scale()
        }, Chosen.prototype.activate_field = function() {
            return this.container.addClass("chosen-container-active"), this.active_field = !0, this.search_field.val(this.search_field.val()), this.search_field.focus()
        }, Chosen.prototype.test_active_click = function(b) {
            return this.container.is(a(b.target).closest(".chosen-container")) ? this.active_field = !0 : this.close_field()
        }, Chosen.prototype.results_build = function() {
            return this.parsing = !0, this.selected_option_count = null, this.results_data = SelectParser.select_to_array(this.form_field), this.is_multiple ? this.search_choices.find("li.search-choice").remove() : this.is_multiple || (this.single_set_selected_text(), this.disable_search || this.form_field.options.length <= this.disable_search_threshold ? (this.search_field[0].readOnly = !0, this.container.addClass("chosen-container-single-nosearch")) : (this.search_field[0].readOnly = !1, this.container.removeClass("chosen-container-single-nosearch"))), this.update_results_content(this.results_option_build({
                first: !0
            })), this.search_field_disabled(), this.show_search_field_default(), this.search_field_scale(), this.parsing = !1
        }, Chosen.prototype.result_do_highlight = function(a) {
            var b, c, d, e, f;
            if (a.length) {
                if (this.result_clear_highlight(), this.result_highlight = a, this.result_highlight.addClass("highlighted"), d = parseInt(this.search_results.css("maxHeight"), 10), f = this.search_results.scrollTop(), e = d + f, c = this.result_highlight.position().top + this.search_results.scrollTop(), b = c + this.result_highlight.outerHeight(), b >= e) return this.search_results.scrollTop(b - d > 0 ? b - d : 0);
                if (f > c) return this.search_results.scrollTop(c)
            }
        }, Chosen.prototype.result_clear_highlight = function() {
            return this.result_highlight && this.result_highlight.removeClass("highlighted"), this.result_highlight = null
        }, Chosen.prototype.results_show = function() {
            return this.is_multiple && this.max_selected_options <= this.choices_count() ? (this.form_field_jq.trigger("chosen:maxselected", {
                chosen: this
            }), !1) : (this.container.addClass("chosen-with-drop"), this.form_field_jq.trigger("chosen:showing_dropdown", {
                chosen: this
            }), this.results_showing = !0, this.search_field.focus(), this.search_field.val(this.search_field.val()), this.winnow_results())
        }, Chosen.prototype.update_results_content = function(a) {
            return this.search_results.html(a)
        }, Chosen.prototype.results_hide = function() {
            return this.results_showing && (this.result_clear_highlight(), this.container.removeClass("chosen-with-drop"), this.form_field_jq.trigger("chosen:hiding_dropdown", {
                chosen: this
            })), this.results_showing = !1
        }, Chosen.prototype.set_tab_index = function() {
            var a;
            return this.form_field.tabIndex ? (a = this.form_field.tabIndex, this.form_field.tabIndex = -1, this.search_field[0].tabIndex = a) : void 0
        }, Chosen.prototype.set_label_behavior = function() {
            var b = this;
            return this.form_field_label = this.form_field_jq.parents("label"), !this.form_field_label.length && this.form_field.id.length && (this.form_field_label = a("label[for='" + this.form_field.id + "']")), this.form_field_label.length > 0 ? this.form_field_label.bind("click.chosen", function(a) {
                return b.is_multiple ? b.container_mousedown(a) : b.activate_field()
            }) : void 0
        }, Chosen.prototype.show_search_field_default = function() {
            return this.is_multiple && this.choices_count() < 1 && !this.active_field ? (this.search_field.val(this.default_text), this.search_field.addClass("default")) : (this.search_field.val(""), this.search_field.removeClass("default"))
        }, Chosen.prototype.search_results_mouseup = function(b) {
            var c;
            return c = a(b.target).hasClass("active-result") ? a(b.target) : a(b.target).parents(".active-result").first(), c.length ? (this.result_highlight = c, this.result_select(b), this.search_field.focus()) : void 0
        }, Chosen.prototype.search_results_mouseover = function(b) {
            var c;
            return c = a(b.target).hasClass("active-result") ? a(b.target) : a(b.target).parents(".active-result").first(), c ? this.result_do_highlight(c) : void 0
        }, Chosen.prototype.search_results_mouseout = function(b) {
            return a(b.target).hasClass("active-result") ? this.result_clear_highlight() : void 0
        }, Chosen.prototype.choice_build = function(b) {
            var c, d, e = this;
            return c = a("<li />", {
                "class": "search-choice"
            }).html("<span>" + b.html + "</span>"), b.disabled ? c.addClass("search-choice-disabled") : (d = a("<a />", {
                "class": "search-choice-close",
                "data-option-array-index": b.array_index
            }), d.bind("click.chosen", function(a) {
                return e.choice_destroy_link_click(a)
            }), c.append(d)), this.search_container.before(c)
        }, Chosen.prototype.choice_destroy_link_click = function(b) {
            return b.preventDefault(), b.stopPropagation(), this.is_disabled ? void 0 : this.choice_destroy(a(b.target))
        }, Chosen.prototype.choice_destroy = function(a) {
            return this.result_deselect(a[0].getAttribute("data-option-array-index")) ? (this.show_search_field_default(), this.is_multiple && this.choices_count() > 0 && this.search_field.val().length < 1 && this.results_hide(), a.parents("li").first().remove(), this.search_field_scale()) : void 0
        }, Chosen.prototype.results_reset = function() {
            return this.form_field.options[0].selected = !0, this.selected_option_count = null, this.single_set_selected_text(), this.show_search_field_default(), this.results_reset_cleanup(), this.form_field_jq.trigger("change"), this.active_field ? this.results_hide() : void 0
        }, Chosen.prototype.results_reset_cleanup = function() {
            return this.current_selectedIndex = this.form_field.selectedIndex, this.selected_item.find("abbr").remove()
        }, Chosen.prototype.result_select = function(a) {
            var b, c, d;
            return this.result_highlight ? (b = this.result_highlight, this.result_clear_highlight(), this.is_multiple && this.max_selected_options <= this.choices_count() ? (this.form_field_jq.trigger("chosen:maxselected", {
                chosen: this
            }), !1) : (this.is_multiple ? b.removeClass("active-result") : (this.result_single_selected && (this.result_single_selected.removeClass("result-selected"), d = this.result_single_selected[0].getAttribute("data-option-array-index"), this.results_data[d].selected = !1), this.result_single_selected = b), b.addClass("result-selected"), c = this.results_data[b[0].getAttribute("data-option-array-index")], c.selected = !0, this.form_field.options[c.options_index].selected = !0, this.selected_option_count = null, this.is_multiple ? this.choice_build(c) : this.single_set_selected_text(c.text), (a.metaKey || a.ctrlKey) && this.is_multiple || this.results_hide(), this.search_field.val(""), (this.is_multiple || this.form_field.selectedIndex !== this.current_selectedIndex) && this.form_field_jq.trigger("change", {
                selected: this.form_field.options[c.options_index].value
            }), this.current_selectedIndex = this.form_field.selectedIndex, this.search_field_scale())) : void 0
        }, Chosen.prototype.single_set_selected_text = function(a) {
            return null == a && (a = this.default_text), a === this.default_text ? this.selected_item.addClass("chosen-default") : (this.single_deselect_control_build(), this.selected_item.removeClass("chosen-default")), this.selected_item.find("span").text(a)
        }, Chosen.prototype.result_deselect = function(a) {
            var b;
            return b = this.results_data[a], this.form_field.options[b.options_index].disabled ? !1 : (b.selected = !1, this.form_field.options[b.options_index].selected = !1, this.selected_option_count = null, this.result_clear_highlight(), this.results_showing && this.winnow_results(), this.form_field_jq.trigger("change", {
                deselected: this.form_field.options[b.options_index].value
            }), this.search_field_scale(), !0)
        }, Chosen.prototype.single_deselect_control_build = function() {
            return this.allow_single_deselect ? (this.selected_item.find("abbr").length || this.selected_item.find("span").first().after('<abbr class="search-choice-close"></abbr>'), this.selected_item.addClass("chosen-single-with-deselect")) : void 0
        }, Chosen.prototype.get_search_text = function() {
            return this.search_field.val() === this.default_text ? "" : a("<div/>").text(a.trim(this.search_field.val())).html()
        }, Chosen.prototype.winnow_results_set_highlight = function() {
            var a, b;
            return b = this.is_multiple ? [] : this.search_results.find(".result-selected.active-result"), a = b.length ? b.first() : this.search_results.find(".active-result").first(), null != a ? this.result_do_highlight(a) : void 0
        }, Chosen.prototype.no_results = function(b) {
            var c;
            return c = a('<li class="no-results">' + this.results_none_found + ' "<span></span>"</li>'), c.find("span").first().html(b), this.search_results.append(c)
        }, Chosen.prototype.no_results_clear = function() {
            return this.search_results.find(".no-results").remove()
        }, Chosen.prototype.keydown_arrow = function() {
            var a;
            return this.results_showing && this.result_highlight ? (a = this.result_highlight.nextAll("li.active-result").first()) ? this.result_do_highlight(a) : void 0 : this.results_show()
        }, Chosen.prototype.keyup_arrow = function() {
            var a;
            return this.results_showing || this.is_multiple ? this.result_highlight ? (a = this.result_highlight.prevAll("li.active-result"), a.length ? this.result_do_highlight(a.first()) : (this.choices_count() > 0 && this.results_hide(), this.result_clear_highlight())) : void 0 : this.results_show()
        }, Chosen.prototype.keydown_backstroke = function() {
            var a;
            return this.pending_backstroke ? (this.choice_destroy(this.pending_backstroke.find("a").first()), this.clear_backstroke()) : (a = this.search_container.siblings("li.search-choice").last(), a.length && !a.hasClass("search-choice-disabled") ? (this.pending_backstroke = a, this.single_backstroke_delete ? this.keydown_backstroke() : this.pending_backstroke.addClass("search-choice-focus")) : void 0)
        }, Chosen.prototype.clear_backstroke = function() {
            return this.pending_backstroke && this.pending_backstroke.removeClass("search-choice-focus"), this.pending_backstroke = null
        }, Chosen.prototype.keydown_checker = function(a) {
            var b, c;
            switch (b = null != (c = a.which) ? c : a.keyCode, this.search_field_scale(), 8 !== b && this.pending_backstroke && this.clear_backstroke(), b) {
                case 8:
                    this.backstroke_length = this.search_field.val().length;
                    break;
                case 9:
                    this.results_showing && !this.is_multiple && this.result_select(a), this.mouse_on_container = !1;
                    break;
                case 13:
                    a.preventDefault();
                    break;
                case 38:
                    a.preventDefault(), this.keyup_arrow();
                    break;
                case 40:
                    a.preventDefault(), this.keydown_arrow()
            }
        }, Chosen.prototype.search_field_scale = function() {
            var b, c, d, e, f, g, h, i, j;
            if (this.is_multiple) {
                for (d = 0, h = 0, f = "position:absolute; left: -1000px; top: -1000px; display:none;", g = ["font-size", "font-style", "font-weight", "font-family", "line-height", "text-transform", "letter-spacing"], i = 0, j = g.length; j > i; i++) e = g[i], f += e + ":" + this.search_field.css(e) + ";";
                return b = a("<div />", {
                    style: f
                }), b.text(this.search_field.val()), a("body").append(b), h = b.width() + 25, b.remove(), c = this.container.outerWidth(), h > c - 10 && (h = c - 10), this.search_field.css({
                    width: h + "px"
                })
            }
        }, Chosen
    }(AbstractChosen)
}.call(this);

/*!
 * @license wysihtml5 v0.3.0
 * https://github.com/xing/wysihtml5
 *
 * Author: Christopher Blum (https://github.com/tiff)
 *
 * Copyright (C) 2012 XING AG
 * Licensed under the MIT license (MIT)
 */
var wysihtml5 = {
    version: "0.3.0",
    commands: {},
    dom: {},
    quirks: {},
    toolbar: {},
    lang: {},
    selection: {},
    views: {},
    INVISIBLE_SPACE: "﻿",
    EMPTY_FUNCTION: function() {},
    ELEMENT_NODE: 1,
    TEXT_NODE: 3,
    BACKSPACE_KEY: 8,
    ENTER_KEY: 13,
    ESCAPE_KEY: 27,
    SPACE_KEY: 32,
    DELETE_KEY: 46
};
window.rangy = function() {
    function e(e, t) {
        var n = typeof e[t];
        return n == c || !(n != u || !e[t]) || "unknown" == n
    }

    function t(e, t) {
        return !(typeof e[t] != u || !e[t])
    }

    function n(e, t) {
        return typeof e[t] != p
    }

    function i(e) {
        return function(t, n) {
            for (var i = n.length; i--;)
                if (!e(t, n[i])) return !1;
            return !0
        }
    }

    function a(e) {
        return e && v(e, g) && b(e, m)
    }

    function r(e) {
        window.alert("Rangy not supported in your browser. Reason: " + e), w.initialized = !0, w.supported = !1
    }

    function o(e) {
        var t = "Rangy warning: " + e;
        w.config.alertOnWarn ? window.alert(t) : typeof window.console != p && typeof window.console.log != p && window.console.log(t)
    }

    function s() {
        if (!w.initialized) {
            var n, i = !1,
                o = !1;
            e(document, "createRange") && (n = document.createRange(), v(n, h) && b(n, f) && (i = !0), n.detach());
            var s = t(document, "body") ? document.body : document.getElementsByTagName("body")[0];
            s && e(s, "createTextRange") && (n = s.createTextRange(), a(n) && (o = !0)), i || o || r("Neither Range nor TextRange are implemented"), w.initialized = !0, w.features = {
                implementsDomRange: i,
                implementsTextRange: o
            };
            for (var d = x.concat(T), l = 0, u = d.length; u > l; ++l) try {
                d[l](w)
            } catch (c) {
                t(window, "console") && e(window.console, "log") && window.console.log("Init listener threw an exception. Continuing.", c)
            }
        }
    }

    function d(e) {
        e = e || window, s();
        for (var t = 0, n = C.length; n > t; ++t) C[t](e)
    }

    function l(e) {
        this.name = e, this.initialized = !1, this.supported = !1
    }
    var u = "object",
        c = "function",
        p = "undefined",
        f = ["startContainer", "startOffset", "endContainer", "endOffset", "collapsed", "commonAncestorContainer", "START_TO_START", "START_TO_END", "END_TO_START", "END_TO_END"],
        h = ["setStart", "setStartBefore", "setStartAfter", "setEnd", "setEndBefore", "setEndAfter", "collapse", "selectNode", "selectNodeContents", "compareBoundaryPoints", "deleteContents", "extractContents", "cloneContents", "insertNode", "surroundContents", "cloneRange", "toString", "detach"],
        m = ["boundingHeight", "boundingLeft", "boundingTop", "boundingWidth", "htmlText", "text"],
        g = ["collapse", "compareEndPoints", "duplicate", "getBookmark", "moveToBookmark", "moveToElementText", "parentElement", "pasteHTML", "select", "setEndPoint", "getBoundingClientRect"],
        v = i(e),
        y = i(t),
        b = i(n),
        w = {
            version: "1.2.2",
            initialized: !1,
            supported: !0,
            util: {
                isHostMethod: e,
                isHostObject: t,
                isHostProperty: n,
                areHostMethods: v,
                areHostObjects: y,
                areHostProperties: b,
                isTextRange: a
            },
            features: {},
            modules: {},
            config: {
                alertOnWarn: !1,
                preferTextRange: !1
            }
        };
    w.fail = r, w.warn = o, {}.hasOwnProperty ? w.util.extend = function(e, t) {
        for (var n in t) t.hasOwnProperty(n) && (e[n] = t[n])
    } : r("hasOwnProperty not supported");
    var T = [],
        x = [];
    w.init = s, w.addInitListener = function(e) {
        w.initialized ? e(w) : T.push(e)
    };
    var C = [];
    w.addCreateMissingNativeApiListener = function(e) {
        C.push(e)
    }, w.createMissingNativeApi = d, l.prototype.fail = function(e) {
        throw this.initialized = !0, this.supported = !1, new Error("Module '" + this.name + "' failed to load: " + e)
    }, l.prototype.warn = function(e) {
        w.warn("Module " + this.name + ": " + e)
    }, l.prototype.createError = function(e) {
        return new Error("Error in Rangy " + this.name + " module: " + e)
    }, w.createModule = function(e, t) {
        var n = new l(e);
        w.modules[e] = n, x.push(function(e) {
            t(e, n), n.initialized = !0, n.supported = !0
        })
    }, w.requireModules = function(e) {
        for (var t, n, i = 0, a = e.length; a > i; ++i) {
            if (n = e[i], t = w.modules[n], !(t && t instanceof l)) throw new Error("Module '" + n + "' not found");
            if (!t.supported) throw new Error("Module '" + n + "' not supported")
        }
    };
    var k = !1,
        E = function() {
            k || (k = !0, w.initialized || s())
        };
    return typeof window == p ? (r("No window found"), void 0) : typeof document == p ? (r("No document found"), void 0) : (e(document, "addEventListener") && document.addEventListener("DOMContentLoaded", E, !1), e(window, "addEventListener") ? window.addEventListener("load", E, !1) : e(window, "attachEvent") ? window.attachEvent("onload", E) : r("Window does not have required addEventListener or attachEvent method"), w)
}(), rangy.createModule("DomUtil", function(e, t) {
    function n(e) {
        var t;
        return typeof e.namespaceURI == E || null === (t = e.namespaceURI) || "http://www.w3.org/1999/xhtml" == t
    }

    function i(e) {
        var t = e.parentNode;
        return 1 == t.nodeType ? t : null
    }

    function a(e) {
        for (var t = 0; e = e.previousSibling;) t++;
        return t
    }

    function r(e) {
        var t;
        return l(e) ? e.length : (t = e.childNodes) ? t.length : 0
    }

    function o(e, t) {
        var n, i = [];
        for (n = e; n; n = n.parentNode) i.push(n);
        for (n = t; n; n = n.parentNode)
            if (D(i, n)) return n;
        return null
    }

    function s(e, t, n) {
        for (var i = n ? t : t.parentNode; i;) {
            if (i === e) return !0;
            i = i.parentNode
        }
        return !1
    }

    function d(e, t, n) {
        for (var i, a = n ? e : e.parentNode; a;) {
            if (i = a.parentNode, i === t) return a;
            a = i
        }
        return null
    }

    function l(e) {
        var t = e.nodeType;
        return 3 == t || 4 == t || 8 == t
    }

    function u(e, t) {
        var n = t.nextSibling,
            i = t.parentNode;
        return n ? i.insertBefore(e, n) : i.appendChild(e), e
    }

    function c(e, t) {
        var n = e.cloneNode(!1);
        return n.deleteData(0, t), e.deleteData(t, e.length - t), u(n, e), n
    }

    function p(e) {
        if (9 == e.nodeType) return e;
        if (typeof e.ownerDocument != E) return e.ownerDocument;
        if (typeof e.document != E) return e.document;
        if (e.parentNode) return p(e.parentNode);
        throw new Error("getDocument: no document found for node")
    }

    function f(e) {
        var t = p(e);
        if (typeof t.defaultView != E) return t.defaultView;
        if (typeof t.parentWindow != E) return t.parentWindow;
        throw new Error("Cannot get a window object for node")
    }

    function h(e) {
        if (typeof e.contentDocument != E) return e.contentDocument;
        if (typeof e.contentWindow != E) return e.contentWindow.document;
        throw new Error("getIframeWindow: No Document object found for iframe element")
    }

    function m(e) {
        if (typeof e.contentWindow != E) return e.contentWindow;
        if (typeof e.contentDocument != E) return e.contentDocument.defaultView;
        throw new Error("getIframeWindow: No Window object found for iframe element")
    }

    function g(e) {
        return N.isHostObject(e, "body") ? e.body : e.getElementsByTagName("body")[0]
    }

    function v(e) {
        for (var t; t = e.parentNode;) e = t;
        return e
    }

    function y(e, t, n, i) {
        var r, s, l, u, c;
        if (e == n) return t === i ? 0 : i > t ? -1 : 1;
        if (r = d(n, e, !0)) return t <= a(r) ? -1 : 1;
        if (r = d(e, n, !0)) return a(r) < i ? -1 : 1;
        if (s = o(e, n), l = e === s ? s : d(e, s, !0), u = n === s ? s : d(n, s, !0), l === u) throw new Error("comparePoints got to case 4 and childA and childB are the same!");
        for (c = s.firstChild; c;) {
            if (c === l) return -1;
            if (c === u) return 1;
            c = c.nextSibling
        }
        throw new Error("Should not be here!")
    }

    function b(e) {
        for (var t, n = p(e).createDocumentFragment(); t = e.firstChild;) n.appendChild(t);
        return n
    }

    function w(e) {
        if (!e) return "[No node]";
        if (l(e)) return '"' + e.data + '"';
        if (1 == e.nodeType) {
            var t = e.id ? ' id="' + e.id + '"' : "";
            return "<" + e.nodeName + t + ">[" + e.childNodes.length + "]"
        }
        return e.nodeName
    }

    function T(e) {
        this.root = e, this._next = e
    }

    function x(e) {
        return new T(e)
    }

    function C(e, t) {
        this.node = e, this.offset = t
    }

    function k(e) {
        this.code = this[e], this.codeName = e, this.message = "DOMException: " + this.codeName
    }
    var E = "undefined",
        N = e.util;
    N.areHostMethods(document, ["createDocumentFragment", "createElement", "createTextNode"]) || t.fail("document missing a Node creation method"), N.isHostMethod(document, "getElementsByTagName") || t.fail("document missing getElementsByTagName method");
    var S = document.createElement("div");
    N.areHostMethods(S, ["insertBefore", "appendChild", "cloneNode"] || !N.areHostObjects(S, ["previousSibling", "nextSibling", "childNodes", "parentNode"])) || t.fail("Incomplete Element implementation"), N.isHostProperty(S, "innerHTML") || t.fail("Element is missing innerHTML property");
    var A = document.createTextNode("test");
    N.areHostMethods(A, ["splitText", "deleteData", "insertData", "appendData", "cloneNode"] || !N.areHostObjects(S, ["previousSibling", "nextSibling", "childNodes", "parentNode"]) || !N.areHostProperties(A, ["data"])) || t.fail("Incomplete Text Node implementation");
    var D = function(e, t) {
        for (var n = e.length; n--;)
            if (e[n] === t) return !0;
        return !1
    };
    T.prototype = {
        _current: null,
        hasNext: function() {
            return !!this._next
        },
        next: function() {
            var e, t, n = this._current = this._next;
            if (this._current)
                if (e = n.firstChild) this._next = e;
                else {
                    for (t = null; n !== this.root && !(t = n.nextSibling);) n = n.parentNode;
                    this._next = t
                }
            return this._current
        },
        detach: function() {
            this._current = this._next = this.root = null
        }
    }, C.prototype = {
        equals: function(e) {
            return this.node === e.node & this.offset == e.offset
        },
        inspect: function() {
            return "[DomPosition(" + w(this.node) + ":" + this.offset + ")]"
        }
    }, k.prototype = {
        INDEX_SIZE_ERR: 1,
        HIERARCHY_REQUEST_ERR: 3,
        WRONG_DOCUMENT_ERR: 4,
        NO_MODIFICATION_ALLOWED_ERR: 7,
        NOT_FOUND_ERR: 8,
        NOT_SUPPORTED_ERR: 9,
        INVALID_STATE_ERR: 11
    }, k.prototype.toString = function() {
        return this.message
    }, e.dom = {
        arrayContains: D,
        isHtmlNamespace: n,
        parentElement: i,
        getNodeIndex: a,
        getNodeLength: r,
        getCommonAncestor: o,
        isAncestorOf: s,
        getClosestAncestorIn: d,
        isCharacterDataNode: l,
        insertAfter: u,
        splitDataNode: c,
        getDocument: p,
        getWindow: f,
        getIframeWindow: m,
        getIframeDocument: h,
        getBody: g,
        getRootContainer: v,
        comparePoints: y,
        inspectNode: w,
        fragmentFromNodeChildren: b,
        createIterator: x,
        DomPosition: C
    }, e.DOMException = k
}), rangy.createModule("DomRange", function(e) {
    function t(e, t) {
        return 3 != e.nodeType && (O.isAncestorOf(e, t.startContainer, !0) || O.isAncestorOf(e, t.endContainer, !0))
    }

    function n(e) {
        return O.getDocument(e.startContainer)
    }

    function i(e, t, n) {
        var i = e._listeners[t];
        if (i)
            for (var a = 0, r = i.length; r > a; ++a) i[a].call(e, {
                target: e,
                args: n
            })
    }

    function a(e) {
        return new H(e.parentNode, O.getNodeIndex(e))
    }

    function r(e) {
        return new H(e.parentNode, O.getNodeIndex(e) + 1)
    }

    function o(e, t, n) {
        var i = 11 == e.nodeType ? e.firstChild : e;
        return O.isCharacterDataNode(t) ? n == t.length ? O.insertAfter(e, t) : t.parentNode.insertBefore(e, 0 == n ? t : O.splitDataNode(t, n)) : n >= t.childNodes.length ? t.appendChild(e) : t.insertBefore(e, t.childNodes[n]), i
    }

    function s(e) {
        for (var t, i, a, r = n(e.range).createDocumentFragment(); i = e.next();) {
            if (t = e.isPartiallySelectedSubtree(), i = i.cloneNode(!t), t && (a = e.getSubtreeIterator(), i.appendChild(s(a)), a.detach(!0)), 10 == i.nodeType) throw new R("HIERARCHY_REQUEST_ERR");
            r.appendChild(i)
        }
        return r
    }

    function d(e, t, n) {
        var i, a;
        n = n || {
            stop: !1
        };
        for (var r, o; r = e.next();)
            if (e.isPartiallySelectedSubtree()) {
                if (t(r) === !1) return n.stop = !0, void 0;
                if (o = e.getSubtreeIterator(), d(o, t, n), o.detach(!0), n.stop) return
            } else
                for (i = O.createIterator(r); a = i.next();)
                    if (t(a) === !1) return n.stop = !0, void 0
    }

    function l(e) {
        for (var t; e.next();) e.isPartiallySelectedSubtree() ? (t = e.getSubtreeIterator(), l(t), t.detach(!0)) : e.remove()
    }

    function u(e) {
        for (var t, i, a = n(e.range).createDocumentFragment(); t = e.next();) {
            if (e.isPartiallySelectedSubtree() ? (t = t.cloneNode(!1), i = e.getSubtreeIterator(), t.appendChild(u(i)), i.detach(!0)) : e.remove(), 10 == t.nodeType) throw new R("HIERARCHY_REQUEST_ERR");
            a.appendChild(t)
        }
        return a
    }

    function c(e, t, n) {
        var i, a = !(!t || !t.length),
            r = !!n;
        a && (i = new RegExp("^(" + t.join("|") + ")$"));
        var o = [];
        return d(new f(e, !1), function(e) {
            a && !i.test(e.nodeType) || r && !n(e) || o.push(e)
        }), o
    }

    function p(e) {
        var t = "undefined" == typeof e.getName ? "Range" : e.getName();
        return "[" + t + "(" + O.inspectNode(e.startContainer) + ":" + e.startOffset + ", " + O.inspectNode(e.endContainer) + ":" + e.endOffset + ")]"
    }

    function f(e, t) {
        if (this.range = e, this.clonePartiallySelectedTextNodes = t, !e.collapsed) {
            this.sc = e.startContainer, this.so = e.startOffset, this.ec = e.endContainer, this.eo = e.endOffset;
            var n = e.commonAncestorContainer;
            this.sc === this.ec && O.isCharacterDataNode(this.sc) ? (this.isSingleCharacterDataNode = !0, this._first = this._last = this._next = this.sc) : (this._first = this._next = this.sc !== n || O.isCharacterDataNode(this.sc) ? O.getClosestAncestorIn(this.sc, n, !0) : this.sc.childNodes[this.so], this._last = this.ec !== n || O.isCharacterDataNode(this.ec) ? O.getClosestAncestorIn(this.ec, n, !0) : this.ec.childNodes[this.eo - 1])
        }
    }

    function h(e) {
        this.code = this[e], this.codeName = e, this.message = "RangeException: " + this.codeName
    }

    function m(e, t, n) {
        this.nodes = c(e, t, n), this._next = this.nodes[0], this._position = 0
    }

    function g(e) {
        return function(t, n) {
            for (var i, a = n ? t : t.parentNode; a;) {
                if (i = a.nodeType, O.arrayContains(e, i)) return a;
                a = a.parentNode
            }
            return null
        }
    }

    function v(e, t) {
        if (Y(e, t)) throw new h("INVALID_NODE_TYPE_ERR")
    }

    function y(e) {
        if (!e.startContainer) throw new R("INVALID_STATE_ERR")
    }

    function b(e, t) {
        if (!O.arrayContains(t, e.nodeType)) throw new h("INVALID_NODE_TYPE_ERR")
    }

    function w(e, t) {
        if (0 > t || t > (O.isCharacterDataNode(e) ? e.length : e.childNodes.length)) throw new R("INDEX_SIZE_ERR")
    }

    function T(e, t) {
        if (U(e, !0) !== U(t, !0)) throw new R("WRONG_DOCUMENT_ERR")
    }

    function x(e) {
        if (W(e, !0)) throw new R("NO_MODIFICATION_ALLOWED_ERR")
    }

    function C(e, t) {
        if (!e) throw new R(t)
    }

    function k(e) {
        return !O.arrayContains(F, e.nodeType) && !U(e, !0)
    }

    function E(e, t) {
        return t <= (O.isCharacterDataNode(e) ? e.length : e.childNodes.length)
    }

    function N(e) {
        if (y(e), k(e.startContainer) || k(e.endContainer) || !E(e.startContainer, e.startOffset) || !E(e.endContainer, e.endOffset)) throw new Error("Range error: Range is no longer valid after DOM mutation (" + e.inspect() + ")")
    }

    function S() {}

    function A(e) {
        e.START_TO_START = Z, e.START_TO_END = et, e.END_TO_END = tt, e.END_TO_START = nt, e.NODE_BEFORE = it, e.NODE_AFTER = at, e.NODE_BEFORE_AND_AFTER = rt, e.NODE_INSIDE = ot
    }

    function D(e) {
        A(e), A(e.prototype)
    }

    function G(e, t) {
        return function() {
            N(this);
            var n, i, a = this.startContainer,
                o = this.startOffset,
                s = this.commonAncestorContainer,
                l = new f(this, !0);
            a !== s && (n = O.getClosestAncestorIn(a, s, !0), i = r(n), a = i.node, o = i.offset), d(l, x), l.reset();
            var u = e(l);
            return l.detach(), t(this, a, o, a, o), u
        }
    }

    function _(n, i, o) {
        function s(e, t) {
            return function(n) {
                y(this), b(n, P), b(z(n), F);
                var i = (e ? a : r)(n);
                (t ? d : c)(this, i.node, i.offset)
            }
        }

        function d(e, t, n) {
            var a = e.endContainer,
                r = e.endOffset;
            (t !== e.startContainer || n !== e.startOffset) && ((z(t) != z(a) || 1 == O.comparePoints(t, n, a, r)) && (a = t, r = n), i(e, t, n, a, r))
        }

        function c(e, t, n) {
            var a = e.startContainer,
                r = e.startOffset;
            (t !== e.endContainer || n !== e.endOffset) && ((z(t) != z(a) || -1 == O.comparePoints(t, n, a, r)) && (a = t, r = n), i(e, a, r, t, n))
        }

        function p(e, t, n) {
            (t !== e.startContainer || n !== e.startOffset || t !== e.endContainer || n !== e.endOffset) && i(e, t, n, t, n)
        }
        n.prototype = new S, e.util.extend(n.prototype, {
            setStart: function(e, t) {
                y(this), v(e, !0), w(e, t), d(this, e, t)
            },
            setEnd: function(e, t) {
                y(this), v(e, !0), w(e, t), c(this, e, t)
            },
            setStartBefore: s(!0, !0),
            setStartAfter: s(!1, !0),
            setEndBefore: s(!0, !1),
            setEndAfter: s(!1, !1),
            collapse: function(e) {
                N(this), e ? i(this, this.startContainer, this.startOffset, this.startContainer, this.startOffset) : i(this, this.endContainer, this.endOffset, this.endContainer, this.endOffset)
            },
            selectNodeContents: function(e) {
                y(this), v(e, !0), i(this, e, 0, e, O.getNodeLength(e))
            },
            selectNode: function(e) {
                y(this), v(e, !1), b(e, P);
                var t = a(e),
                    n = r(e);
                i(this, t.node, t.offset, n.node, n.offset)
            },
            extractContents: G(u, i),
            deleteContents: G(l, i),
            canSurroundContents: function() {
                N(this), x(this.startContainer), x(this.endContainer);
                var e = new f(this, !0),
                    n = e._first && t(e._first, this) || e._last && t(e._last, this);
                return e.detach(), !n
            },
            detach: function() {
                o(this)
            },
            splitBoundaries: function() {
                N(this);
                var e = this.startContainer,
                    t = this.startOffset,
                    n = this.endContainer,
                    a = this.endOffset,
                    r = e === n;
                O.isCharacterDataNode(n) && a > 0 && a < n.length && O.splitDataNode(n, a), O.isCharacterDataNode(e) && t > 0 && t < e.length && (e = O.splitDataNode(e, t), r ? (a -= t, n = e) : n == e.parentNode && a >= O.getNodeIndex(e) && a++, t = 0), i(this, e, t, n, a)
            },
            normalizeBoundaries: function() {
                N(this);
                var e = this.startContainer,
                    t = this.startOffset,
                    n = this.endContainer,
                    a = this.endOffset,
                    r = function(e) {
                        var t = e.nextSibling;
                        t && t.nodeType == e.nodeType && (n = e, a = e.length, e.appendData(t.data), t.parentNode.removeChild(t))
                    },
                    o = function(i) {
                        var r = i.previousSibling;
                        if (r && r.nodeType == i.nodeType) {
                            e = i;
                            var o = i.length;
                            if (t = r.length, i.insertData(0, r.data), r.parentNode.removeChild(r), e == n) a += t, n = e;
                            else if (n == i.parentNode) {
                                var s = O.getNodeIndex(i);
                                a == s ? (n = i, a = o) : a > s && a--
                            }
                        }
                    },
                    s = !0;
                if (O.isCharacterDataNode(n)) n.length == a && r(n);
                else {
                    if (a > 0) {
                        var d = n.childNodes[a - 1];
                        d && O.isCharacterDataNode(d) && r(d)
                    }
                    s = !this.collapsed
                }
                if (s) {
                    if (O.isCharacterDataNode(e)) 0 == t && o(e);
                    else if (t < e.childNodes.length) {
                        var l = e.childNodes[t];
                        l && O.isCharacterDataNode(l) && o(l)
                    }
                } else e = n, t = a;
                i(this, e, t, n, a)
            },
            collapseToPoint: function(e, t) {
                y(this), v(e, !0), w(e, t), p(this, e, t)
            }
        }), D(n)
    }

    function j(e) {
        e.collapsed = e.startContainer === e.endContainer && e.startOffset === e.endOffset, e.commonAncestorContainer = e.collapsed ? e.startContainer : O.getCommonAncestor(e.startContainer, e.endContainer)
    }

    function M(e, t, n, a, r) {
        var o = e.startContainer !== t || e.startOffset !== n,
            s = e.endContainer !== a || e.endOffset !== r;
        e.startContainer = t, e.startOffset = n, e.endContainer = a, e.endOffset = r, j(e), i(e, "boundarychange", {
            startMoved: o,
            endMoved: s
        })
    }

    function L(e) {
        y(e), e.startContainer = e.startOffset = e.endContainer = e.endOffset = null, e.collapsed = e.commonAncestorContainer = null, i(e, "detach", null), e._listeners = null
    }

    function I(e) {
        this.startContainer = e, this.startOffset = 0, this.endContainer = e, this.endOffset = 0, this._listeners = {
            boundarychange: [],
            detach: []
        }, j(this)
    }
    e.requireModules(["DomUtil"]);
    var O = e.dom,
        H = O.DomPosition,
        R = e.DOMException;
    f.prototype = {
        _current: null,
        _next: null,
        _first: null,
        _last: null,
        isSingleCharacterDataNode: !1,
        reset: function() {
            this._current = null, this._next = this._first
        },
        hasNext: function() {
            return !!this._next
        },
        next: function() {
            var e = this._current = this._next;
            return e && (this._next = e !== this._last ? e.nextSibling : null, O.isCharacterDataNode(e) && this.clonePartiallySelectedTextNodes && (e === this.ec && (e = e.cloneNode(!0)).deleteData(this.eo, e.length - this.eo), this._current === this.sc && (e = e.cloneNode(!0)).deleteData(0, this.so))), e
        },
        remove: function() {
            var e, t, n = this._current;
            !O.isCharacterDataNode(n) || n !== this.sc && n !== this.ec ? n.parentNode && n.parentNode.removeChild(n) : (e = n === this.sc ? this.so : 0, t = n === this.ec ? this.eo : n.length, e != t && n.deleteData(e, t - e))
        },
        isPartiallySelectedSubtree: function() {
            var e = this._current;
            return t(e, this.range)
        },
        getSubtreeIterator: function() {
            var e;
            if (this.isSingleCharacterDataNode) e = this.range.cloneRange(), e.collapse();
            else {
                e = new I(n(this.range));
                var t = this._current,
                    i = t,
                    a = 0,
                    r = t,
                    o = O.getNodeLength(t);
                O.isAncestorOf(t, this.sc, !0) && (i = this.sc, a = this.so), O.isAncestorOf(t, this.ec, !0) && (r = this.ec, o = this.eo), M(e, i, a, r, o)
            }
            return new f(e, this.clonePartiallySelectedTextNodes)
        },
        detach: function(e) {
            e && this.range.detach(), this.range = this._current = this._next = this._first = this._last = this.sc = this.so = this.ec = this.eo = null
        }
    }, h.prototype = {
        BAD_BOUNDARYPOINTS_ERR: 1,
        INVALID_NODE_TYPE_ERR: 2
    }, h.prototype.toString = function() {
        return this.message
    }, m.prototype = {
        _current: null,
        hasNext: function() {
            return !!this._next
        },
        next: function() {
            return this._current = this._next, this._next = this.nodes[++this._position], this._current
        },
        detach: function() {
            this._current = this._next = this.nodes = null
        }
    };
    var P = [1, 3, 4, 5, 7, 8, 10],
        F = [2, 9, 11],
        $ = [5, 6, 10, 12],
        B = [1, 3, 4, 5, 7, 8, 10, 11],
        q = [1, 3, 4, 5, 7, 8],
        z = O.getRootContainer,
        U = g([9, 11]),
        W = g($),
        Y = g([6, 10, 12]),
        X = document.createElement("style"),
        Q = !1;
    try {
        X.innerHTML = "<b>x</b>", Q = 3 == X.firstChild.nodeType
    } catch (V) {}
    e.features.htmlParsingConforms = Q;
    var J = Q ? function(e) {
            var t = this.startContainer,
                n = O.getDocument(t);
            if (!t) throw new R("INVALID_STATE_ERR");
            var i = null;
            return 1 == t.nodeType ? i = t : O.isCharacterDataNode(t) && (i = O.parentElement(t)), i = null === i || "HTML" == i.nodeName && O.isHtmlNamespace(O.getDocument(i).documentElement) && O.isHtmlNamespace(i) ? n.createElement("body") : i.cloneNode(!1), i.innerHTML = e, O.fragmentFromNodeChildren(i)
        } : function(e) {
            y(this);
            var t = n(this),
                i = t.createElement("body");
            return i.innerHTML = e, O.fragmentFromNodeChildren(i)
        },
        K = ["startContainer", "startOffset", "endContainer", "endOffset", "collapsed", "commonAncestorContainer"],
        Z = 0,
        et = 1,
        tt = 2,
        nt = 3,
        it = 0,
        at = 1,
        rt = 2,
        ot = 3;
    S.prototype = {
        attachListener: function(e, t) {
            this._listeners[e].push(t)
        },
        compareBoundaryPoints: function(e, t) {
            N(this), T(this.startContainer, t.startContainer);
            var n, i, a, r, o = e == nt || e == Z ? "start" : "end",
                s = e == et || e == Z ? "start" : "end";
            return n = this[o + "Container"], i = this[o + "Offset"], a = t[s + "Container"], r = t[s + "Offset"], O.comparePoints(n, i, a, r)
        },
        insertNode: function(e) {
            if (N(this), b(e, B), x(this.startContainer), O.isAncestorOf(e, this.startContainer, !0)) throw new R("HIERARCHY_REQUEST_ERR");
            var t = o(e, this.startContainer, this.startOffset);
            this.setStartBefore(t)
        },
        cloneContents: function() {
            N(this);
            var e, t;
            if (this.collapsed) return n(this).createDocumentFragment();
            if (this.startContainer === this.endContainer && O.isCharacterDataNode(this.startContainer)) return e = this.startContainer.cloneNode(!0), e.data = e.data.slice(this.startOffset, this.endOffset), t = n(this).createDocumentFragment(), t.appendChild(e), t;
            var i = new f(this, !0);
            return e = s(i), i.detach(), e
        },
        canSurroundContents: function() {
            N(this), x(this.startContainer), x(this.endContainer);
            var e = new f(this, !0),
                n = e._first && t(e._first, this) || e._last && t(e._last, this);
            return e.detach(), !n
        },
        surroundContents: function(e) {
            if (b(e, q), !this.canSurroundContents()) throw new h("BAD_BOUNDARYPOINTS_ERR");
            var t = this.extractContents();
            if (e.hasChildNodes())
                for (; e.lastChild;) e.removeChild(e.lastChild);
            o(e, this.startContainer, this.startOffset), e.appendChild(t), this.selectNode(e)
        },
        cloneRange: function() {
            N(this);
            for (var e, t = new I(n(this)), i = K.length; i--;) e = K[i], t[e] = this[e];
            return t
        },
        toString: function() {
            N(this);
            var e = this.startContainer;
            if (e === this.endContainer && O.isCharacterDataNode(e)) return 3 == e.nodeType || 4 == e.nodeType ? e.data.slice(this.startOffset, this.endOffset) : "";
            var t = [],
                n = new f(this, !0);
            return d(n, function(e) {
                (3 == e.nodeType || 4 == e.nodeType) && t.push(e.data)
            }), n.detach(), t.join("")
        },
        compareNode: function(e) {
            N(this);
            var t = e.parentNode,
                n = O.getNodeIndex(e);
            if (!t) throw new R("NOT_FOUND_ERR");
            var i = this.comparePoint(t, n),
                a = this.comparePoint(t, n + 1);
            return 0 > i ? a > 0 ? rt : it : a > 0 ? at : ot
        },
        comparePoint: function(e, t) {
            return N(this), C(e, "HIERARCHY_REQUEST_ERR"), T(e, this.startContainer), O.comparePoints(e, t, this.startContainer, this.startOffset) < 0 ? -1 : O.comparePoints(e, t, this.endContainer, this.endOffset) > 0 ? 1 : 0
        },
        createContextualFragment: J,
        toHtml: function() {
            N(this);
            var e = n(this).createElement("div");
            return e.appendChild(this.cloneContents()), e.innerHTML
        },
        intersectsNode: function(e, t) {
            if (N(this), C(e, "NOT_FOUND_ERR"), O.getDocument(e) !== n(this)) return !1;
            var i = e.parentNode,
                a = O.getNodeIndex(e);
            C(i, "NOT_FOUND_ERR");
            var r = O.comparePoints(i, a, this.endContainer, this.endOffset),
                o = O.comparePoints(i, a + 1, this.startContainer, this.startOffset);
            return t ? 0 >= r && o >= 0 : 0 > r && o > 0
        },
        isPointInRange: function(e, t) {
            return N(this), C(e, "HIERARCHY_REQUEST_ERR"), T(e, this.startContainer), O.comparePoints(e, t, this.startContainer, this.startOffset) >= 0 && O.comparePoints(e, t, this.endContainer, this.endOffset) <= 0
        },
        intersectsRange: function(e, t) {
            if (N(this), n(e) != n(this)) throw new R("WRONG_DOCUMENT_ERR");
            var i = O.comparePoints(this.startContainer, this.startOffset, e.endContainer, e.endOffset),
                a = O.comparePoints(this.endContainer, this.endOffset, e.startContainer, e.startOffset);
            return t ? 0 >= i && a >= 0 : 0 > i && a > 0
        },
        intersection: function(e) {
            if (this.intersectsRange(e)) {
                var t = O.comparePoints(this.startContainer, this.startOffset, e.startContainer, e.startOffset),
                    n = O.comparePoints(this.endContainer, this.endOffset, e.endContainer, e.endOffset),
                    i = this.cloneRange();
                return -1 == t && i.setStart(e.startContainer, e.startOffset), 1 == n && i.setEnd(e.endContainer, e.endOffset), i
            }
            return null
        },
        union: function(e) {
            if (this.intersectsRange(e, !0)) {
                var t = this.cloneRange();
                return -1 == O.comparePoints(e.startContainer, e.startOffset, this.startContainer, this.startOffset) && t.setStart(e.startContainer, e.startOffset), 1 == O.comparePoints(e.endContainer, e.endOffset, this.endContainer, this.endOffset) && t.setEnd(e.endContainer, e.endOffset), t
            }
            throw new h("Ranges do not intersect")
        },
        containsNode: function(e, t) {
            return t ? this.intersectsNode(e, !1) : this.compareNode(e) == ot
        },
        containsNodeContents: function(e) {
            return this.comparePoint(e, 0) >= 0 && this.comparePoint(e, O.getNodeLength(e)) <= 0
        },
        containsRange: function(e) {
            return this.intersection(e).equals(e)
        },
        containsNodeText: function(e) {
            var t = this.cloneRange();
            t.selectNode(e);
            var n = t.getNodes([3]);
            if (n.length > 0) {
                t.setStart(n[0], 0);
                var i = n.pop();
                t.setEnd(i, i.length);
                var a = this.containsRange(t);
                return t.detach(), a
            }
            return this.containsNodeContents(e)
        },
        createNodeIterator: function(e, t) {
            return N(this), new m(this, e, t)
        },
        getNodes: function(e, t) {
            return N(this), c(this, e, t)
        },
        getDocument: function() {
            return n(this)
        },
        collapseBefore: function(e) {
            y(this), this.setEndBefore(e), this.collapse(!1)
        },
        collapseAfter: function(e) {
            y(this), this.setStartAfter(e), this.collapse(!0)
        },
        getName: function() {
            return "DomRange"
        },
        equals: function(e) {
            return I.rangesEqual(this, e)
        },
        inspect: function() {
            return p(this)
        }
    }, _(I, M, L), e.rangePrototype = S.prototype, I.rangeProperties = K, I.RangeIterator = f, I.copyComparisonConstants = D, I.createPrototypeRange = _, I.inspect = p, I.getRangeDocument = n, I.rangesEqual = function(e, t) {
        return e.startContainer === t.startContainer && e.startOffset === t.startOffset && e.endContainer === t.endContainer && e.endOffset === t.endOffset
    }, e.DomRange = I, e.RangeException = h
}), rangy.createModule("WrappedRange", function(e) {
    function t(e) {
        var t = e.parentElement(),
            n = e.duplicate();
        n.collapse(!0);
        var i = n.parentElement();
        n = e.duplicate(), n.collapse(!1);
        var a = n.parentElement(),
            r = i == a ? i : o.getCommonAncestor(i, a);
        return r == t ? r : o.getCommonAncestor(t, r)
    }

    function n(e) {
        return 0 == e.compareEndPoints("StartToEnd", e)
    }

    function i(e, t, n, i) {
        var a = e.duplicate();
        a.collapse(n);
        var r = a.parentElement();
        if (o.isAncestorOf(t, r, !0) || (r = t), !r.canHaveHTML) return new s(r.parentNode, o.getNodeIndex(r));
        var d, l, u, c, p, f = o.getDocument(r).createElement("span"),
            h = n ? "StartToStart" : "StartToEnd";
        do r.insertBefore(f, f.previousSibling), a.moveToElementText(f); while ((d = a.compareEndPoints(h, e)) > 0 && f.previousSibling);
        if (p = f.nextSibling, -1 == d && p && o.isCharacterDataNode(p)) {
            a.setEndPoint(n ? "EndToStart" : "EndToEnd", e);
            var m;
            if (/[\r\n]/.test(p.data)) {
                var g = a.duplicate(),
                    v = g.text.replace(/\r\n/g, "\r").length;
                for (m = g.moveStart("character", v); - 1 == (d = g.compareEndPoints("StartToEnd", g));) m++, g.moveStart("character", 1)
            } else m = a.text.length;
            c = new s(p, m)
        } else l = (i || !n) && f.previousSibling, u = (i || n) && f.nextSibling, c = u && o.isCharacterDataNode(u) ? new s(u, 0) : l && o.isCharacterDataNode(l) ? new s(l, l.length) : new s(r, o.getNodeIndex(f));
        return f.parentNode.removeChild(f), c
    }

    function a(e, t) {
        var n, i, a, r, s = e.offset,
            d = o.getDocument(e.node),
            l = d.body.createTextRange(),
            u = o.isCharacterDataNode(e.node);
        return u ? (n = e.node, i = n.parentNode) : (r = e.node.childNodes, n = s < r.length ? r[s] : null, i = e.node), a = d.createElement("span"), a.innerHTML = "&#feff;", n ? i.insertBefore(a, n) : i.appendChild(a), l.moveToElementText(a), l.collapse(!t), i.removeChild(a), u && l[t ? "moveStart" : "moveEnd"]("character", s), l
    }
    e.requireModules(["DomUtil", "DomRange"]);
    var r, o = e.dom,
        s = o.DomPosition,
        d = e.DomRange;
    if (!e.features.implementsDomRange || e.features.implementsTextRange && e.config.preferTextRange) {
        if (e.features.implementsTextRange) {
            r = function(e) {
                this.textRange = e, this.refresh()
            }, r.prototype = new d(document), r.prototype.refresh = function() {
                var e, a, r = t(this.textRange);
                n(this.textRange) ? a = e = i(this.textRange, r, !0, !0) : (e = i(this.textRange, r, !0, !1), a = i(this.textRange, r, !1, !1)), this.setStart(e.node, e.offset), this.setEnd(a.node, a.offset)
            }, d.copyComparisonConstants(r);
            var l = function() {
                return this
            }();
            "undefined" == typeof l.Range && (l.Range = r), e.createNativeRange = function(e) {
                return e = e || document, e.body.createTextRange()
            }
        }
    } else ! function() {
        function t(e) {
            for (var t, n = u.length; n--;) t = u[n], e[t] = e.nativeRange[t]
        }

        function n(e, t, n, i, a) {
            var r = e.startContainer !== t || e.startOffset != n,
                o = e.endContainer !== i || e.endOffset != a;
            (r || o) && (e.setEnd(i, a), e.setStart(t, n))
        }

        function i(e) {
            e.nativeRange.detach(), e.detached = !0;
            for (var t, n = u.length; n--;) t = u[n], e[t] = null
        }
        var a, s, l, u = d.rangeProperties;
        r = function(e) {
            if (!e) throw new Error("Range must be specified");
            this.nativeRange = e, t(this)
        }, d.createPrototypeRange(r, n, i), a = r.prototype, a.selectNode = function(e) {
            this.nativeRange.selectNode(e), t(this)
        }, a.deleteContents = function() {
            this.nativeRange.deleteContents(), t(this)
        }, a.extractContents = function() {
            var e = this.nativeRange.extractContents();
            return t(this), e
        }, a.cloneContents = function() {
            return this.nativeRange.cloneContents()
        }, a.surroundContents = function(e) {
            this.nativeRange.surroundContents(e), t(this)
        }, a.collapse = function(e) {
            this.nativeRange.collapse(e), t(this)
        }, a.cloneRange = function() {
            return new r(this.nativeRange.cloneRange())
        }, a.refresh = function() {
            t(this)
        }, a.toString = function() {
            return this.nativeRange.toString()
        };
        var c = document.createTextNode("test");
        o.getBody(document).appendChild(c);
        var p = document.createRange();
        p.setStart(c, 0), p.setEnd(c, 0);
        try {
            p.setStart(c, 1), s = !0, a.setStart = function(e, n) {
                this.nativeRange.setStart(e, n), t(this)
            }, a.setEnd = function(e, n) {
                this.nativeRange.setEnd(e, n), t(this)
            }, l = function(e) {
                return function(n) {
                    this.nativeRange[e](n), t(this)
                }
            }
        } catch (f) {
            s = !1, a.setStart = function(e, n) {
                try {
                    this.nativeRange.setStart(e, n)
                } catch (i) {
                    this.nativeRange.setEnd(e, n), this.nativeRange.setStart(e, n)
                }
                t(this)
            }, a.setEnd = function(e, n) {
                try {
                    this.nativeRange.setEnd(e, n)
                } catch (i) {
                    this.nativeRange.setStart(e, n), this.nativeRange.setEnd(e, n)
                }
                t(this)
            }, l = function(e, n) {
                return function(i) {
                    try {
                        this.nativeRange[e](i)
                    } catch (a) {
                        this.nativeRange[n](i), this.nativeRange[e](i)
                    }
                    t(this)
                }
            }
        }
        a.setStartBefore = l("setStartBefore", "setEndBefore"), a.setStartAfter = l("setStartAfter", "setEndAfter"), a.setEndBefore = l("setEndBefore", "setStartBefore"), a.setEndAfter = l("setEndAfter", "setStartAfter"), p.selectNodeContents(c), a.selectNodeContents = p.startContainer == c && p.endContainer == c && 0 == p.startOffset && p.endOffset == c.length ? function(e) {
            this.nativeRange.selectNodeContents(e), t(this)
        } : function(e) {
            this.setStart(e, 0), this.setEnd(e, d.getEndOffset(e))
        }, p.selectNodeContents(c), p.setEnd(c, 3);
        var h = document.createRange();
        h.selectNodeContents(c), h.setEnd(c, 4), h.setStart(c, 2), a.compareBoundaryPoints = -1 == p.compareBoundaryPoints(p.START_TO_END, h) & 1 == p.compareBoundaryPoints(p.END_TO_START, h) ? function(e, t) {
            return t = t.nativeRange || t, e == t.START_TO_END ? e = t.END_TO_START : e == t.END_TO_START && (e = t.START_TO_END), this.nativeRange.compareBoundaryPoints(e, t)
        } : function(e, t) {
            return this.nativeRange.compareBoundaryPoints(e, t.nativeRange || t)
        }, e.util.isHostMethod(p, "createContextualFragment") && (a.createContextualFragment = function(e) {
            return this.nativeRange.createContextualFragment(e)
        }), o.getBody(document).removeChild(c), p.detach(), h.detach()
    }(), e.createNativeRange = function(e) {
        return e = e || document, e.createRange()
    };
    e.features.implementsTextRange && (r.rangeToTextRange = function(e) {
        if (e.collapsed) {
            var t = a(new s(e.startContainer, e.startOffset), !0);
            return t
        }
        var n = a(new s(e.startContainer, e.startOffset), !0),
            i = a(new s(e.endContainer, e.endOffset), !1),
            r = o.getDocument(e.startContainer).body.createTextRange();
        return r.setEndPoint("StartToStart", n), r.setEndPoint("EndToEnd", i), r
    }), r.prototype.getName = function() {
        return "WrappedRange"
    }, e.WrappedRange = r, e.createRange = function(t) {
        return t = t || document, new r(e.createNativeRange(t))
    }, e.createRangyRange = function(e) {
        return e = e || document, new d(e)
    }, e.createIframeRange = function(t) {
        return e.createRange(o.getIframeDocument(t))
    }, e.createIframeRangyRange = function(t) {
        return e.createRangyRange(o.getIframeDocument(t))
    }, e.addCreateMissingNativeApiListener(function(t) {
        var n = t.document;
        "undefined" == typeof n.createRange && (n.createRange = function() {
            return e.createRange(this)
        }), n = t = null
    })
}), rangy.createModule("WrappedSelection", function(e, t) {
    function n(e) {
        return (e || window).getSelection()
    }

    function i(e) {
        return (e || window).document.selection
    }

    function a(e, t, n) {
        var i = n ? "end" : "start",
            a = n ? "start" : "end";
        e.anchorNode = t[i + "Container"], e.anchorOffset = t[i + "Offset"], e.focusNode = t[a + "Container"], e.focusOffset = t[a + "Offset"]
    }

    function r(e) {
        var t = e.nativeSelection;
        e.anchorNode = t.anchorNode, e.anchorOffset = t.anchorOffset, e.focusNode = t.focusNode, e.focusOffset = t.focusOffset
    }

    function o(e) {
        e.anchorNode = e.focusNode = null, e.anchorOffset = e.focusOffset = 0, e.rangeCount = 0, e.isCollapsed = !0, e._ranges.length = 0
    }

    function s(t) {
        var n;
        return t instanceof k ? (n = t._selectionNativeRange, n || (n = e.createNativeRange(x.getDocument(t.startContainer)), n.setEnd(t.endContainer, t.endOffset), n.setStart(t.startContainer, t.startOffset), t._selectionNativeRange = n, t.attachListener("detach", function() {
            this._selectionNativeRange = null
        }))) : t instanceof E ? n = t.nativeRange : e.features.implementsDomRange && t instanceof x.getWindow(t.startContainer).Range && (n = t), n
    }

    function d(e) {
        if (!e.length || 1 != e[0].nodeType) return !1;
        for (var t = 1, n = e.length; n > t; ++t)
            if (!x.isAncestorOf(e[0], e[t])) return !1;
        return !0
    }

    function l(e) {
        var t = e.getNodes();
        if (!d(t)) throw new Error("getSingleElementFromRange: range " + e.inspect() + " did not consist of a single element");
        return t[0]
    }

    function u(e) {
        return !!e && "undefined" != typeof e.text
    }

    function c(e, t) {
        var n = new E(t);
        e._ranges = [n], a(e, n, !1), e.rangeCount = 1, e.isCollapsed = n.collapsed
    }

    function p(t) {
        if (t._ranges.length = 0, "None" == t.docSelection.type) o(t);
        else {
            var n = t.docSelection.createRange();
            if (u(n)) c(t, n);
            else {
                t.rangeCount = n.length;
                for (var i, r = x.getDocument(n.item(0)), s = 0; s < t.rangeCount; ++s) i = e.createRange(r), i.selectNode(n.item(s)), t._ranges.push(i);
                t.isCollapsed = 1 == t.rangeCount && t._ranges[0].collapsed, a(t, t._ranges[t.rangeCount - 1], !1)
            }
        }
    }

    function f(e, t) {
        for (var n = e.docSelection.createRange(), i = l(t), a = x.getDocument(n.item(0)), r = x.getBody(a).createControlRange(), o = 0, s = n.length; s > o; ++o) r.add(n.item(o));
        try {
            r.add(i)
        } catch (d) {
            throw new Error("addRange(): Element within the specified Range could not be added to control selection (does it have layout?)")
        }
        r.select(), p(e)
    }

    function h(e, t, n) {
        this.nativeSelection = e, this.docSelection = t, this._ranges = [], this.win = n, this.refresh()
    }

    function m(e, t) {
        for (var n, i = x.getDocument(t[0].startContainer), a = x.getBody(i).createControlRange(), r = 0; rangeCount > r; ++r) {
            n = l(t[r]);
            try {
                a.add(n)
            } catch (o) {
                throw new Error("setRanges(): Element within the one of the specified Ranges could not be added to control selection (does it have layout?)")
            }
        }
        a.select(), p(e)
    }

    function g(e, t) {
        if (e.anchorNode && x.getDocument(e.anchorNode) !== x.getDocument(t)) throw new N("WRONG_DOCUMENT_ERR")
    }

    function v(e) {
        var t = [],
            n = new S(e.anchorNode, e.anchorOffset),
            i = new S(e.focusNode, e.focusOffset),
            a = "function" == typeof e.getName ? e.getName() : "Selection";
        if ("undefined" != typeof e.rangeCount)
            for (var r = 0, o = e.rangeCount; o > r; ++r) t[r] = k.inspect(e.getRangeAt(r));
        return "[" + a + "(Ranges: " + t.join(", ") + ")(anchor: " + n.inspect() + ", focus: " + i.inspect() + "]"
    }
    e.requireModules(["DomUtil", "DomRange", "WrappedRange"]), e.config.checkSelectionRanges = !0;
    var y, b, w = "boolean",
        T = "_rangySelection",
        x = e.dom,
        C = e.util,
        k = e.DomRange,
        E = e.WrappedRange,
        N = e.DOMException,
        S = x.DomPosition,
        A = "Control",
        D = e.util.isHostMethod(window, "getSelection"),
        G = e.util.isHostObject(document, "selection"),
        _ = G && (!D || e.config.preferTextRange);
    _ ? (y = i, e.isSelectionValid = function(e) {
        var t = (e || window).document,
            n = t.selection;
        return "None" != n.type || x.getDocument(n.createRange().parentElement()) == t
    }) : D ? (y = n, e.isSelectionValid = function() {
        return !0
    }) : t.fail("Neither document.selection or window.getSelection() detected."), e.getNativeSelection = y;
    var j = y(),
        M = e.createNativeRange(document),
        L = x.getBody(document),
        I = C.areHostObjects(j, ["anchorNode", "focusNode"] && C.areHostProperties(j, ["anchorOffset", "focusOffset"]));
    e.features.selectionHasAnchorAndFocus = I;
    var O = C.isHostMethod(j, "extend");
    e.features.selectionHasExtend = O;
    var H = "number" == typeof j.rangeCount;
    e.features.selectionHasRangeCount = H;
    var R = !1,
        P = !0;
    C.areHostMethods(j, ["addRange", "getRangeAt", "removeAllRanges"]) && "number" == typeof j.rangeCount && e.features.implementsDomRange && function() {
        var e = document.createElement("iframe");
        L.appendChild(e);
        var t = x.getIframeDocument(e);
        t.open(), t.write("<html><head></head><body>12</body></html>"), t.close();
        var n = x.getIframeWindow(e).getSelection(),
            i = t.documentElement,
            a = i.lastChild,
            r = a.firstChild,
            o = t.createRange();
        o.setStart(r, 1), o.collapse(!0), n.addRange(o), P = 1 == n.rangeCount, n.removeAllRanges();
        var s = o.cloneRange();
        o.setStart(r, 0), s.setEnd(r, 2), n.addRange(o), n.addRange(s), R = 2 == n.rangeCount, o.detach(), s.detach(), L.removeChild(e)
    }(), e.features.selectionSupportsMultipleRanges = R, e.features.collapsedNonEditableSelectionsSupported = P;
    var F, $ = !1;
    L && C.isHostMethod(L, "createControlRange") && (F = L.createControlRange(), C.areHostProperties(F, ["item", "add"]) && ($ = !0)), e.features.implementsControlRange = $, b = I ? function(e) {
        return e.anchorNode === e.focusNode && e.anchorOffset === e.focusOffset
    } : function(e) {
        return e.rangeCount ? e.getRangeAt(e.rangeCount - 1).collapsed : !1
    };
    var B;
    C.isHostMethod(j, "getRangeAt") ? B = function(e, t) {
        try {
            return e.getRangeAt(t)
        } catch (n) {
            return null
        }
    } : I && (B = function(t) {
        var n = x.getDocument(t.anchorNode),
            i = e.createRange(n);
        return i.setStart(t.anchorNode, t.anchorOffset), i.setEnd(t.focusNode, t.focusOffset), i.collapsed !== this.isCollapsed && (i.setStart(t.focusNode, t.focusOffset), i.setEnd(t.anchorNode, t.anchorOffset)), i
    }), e.getSelection = function(e) {
        e = e || window;
        var t = e[T],
            n = y(e),
            a = G ? i(e) : null;
        return t ? (t.nativeSelection = n, t.docSelection = a, t.refresh(e)) : (t = new h(n, a, e), e[T] = t), t
    }, e.getIframeSelection = function(t) {
        return e.getSelection(x.getIframeWindow(t))
    };
    var q = h.prototype;
    if (!_ && I && C.areHostMethods(j, ["removeAllRanges", "addRange"])) {
        q.removeAllRanges = function() {
            this.nativeSelection.removeAllRanges(), o(this)
        };
        var z = function(t, n) {
            var i = k.getRangeDocument(n),
                a = e.createRange(i);
            a.collapseToPoint(n.endContainer, n.endOffset), t.nativeSelection.addRange(s(a)), t.nativeSelection.extend(n.startContainer, n.startOffset), t.refresh()
        };
        q.addRange = H ? function(t, n) {
            if ($ && G && this.docSelection.type == A) f(this, t);
            else if (n && O) z(this, t);
            else {
                var i;
                if (R ? i = this.rangeCount : (this.removeAllRanges(), i = 0), this.nativeSelection.addRange(s(t)), this.rangeCount = this.nativeSelection.rangeCount, this.rangeCount == i + 1) {
                    if (e.config.checkSelectionRanges) {
                        var r = B(this.nativeSelection, this.rangeCount - 1);
                        r && !k.rangesEqual(r, t) && (t = new E(r))
                    }
                    this._ranges[this.rangeCount - 1] = t, a(this, t, Y(this.nativeSelection)), this.isCollapsed = b(this)
                } else this.refresh()
            }
        } : function(e, t) {
            t && O ? z(this, e) : (this.nativeSelection.addRange(s(e)), this.refresh())
        }, q.setRanges = function(e) {
            if ($ && e.length > 1) m(this, e);
            else {
                this.removeAllRanges();
                for (var t = 0, n = e.length; n > t; ++t) this.addRange(e[t])
            }
        }
    } else {
        if (!(C.isHostMethod(j, "empty") && C.isHostMethod(M, "select") && $ && _)) return t.fail("No means of selecting a Range or TextRange was found"), !1;
        q.removeAllRanges = function() {
            try {
                if (this.docSelection.empty(), "None" != this.docSelection.type) {
                    var e;
                    if (this.anchorNode) e = x.getDocument(this.anchorNode);
                    else if (this.docSelection.type == A) {
                        var t = this.docSelection.createRange();
                        t.length && (e = x.getDocument(t.item(0)).body.createTextRange())
                    }
                    if (e) {
                        var n = e.body.createTextRange();
                        n.select(), this.docSelection.empty()
                    }
                }
            } catch (i) {}
            o(this)
        }, q.addRange = function(e) {
            this.docSelection.type == A ? f(this, e) : (E.rangeToTextRange(e).select(), this._ranges[0] = e, this.rangeCount = 1, this.isCollapsed = this._ranges[0].collapsed, a(this, e, !1))
        }, q.setRanges = function(e) {
            this.removeAllRanges();
            var t = e.length;
            t > 1 ? m(this, e) : t && this.addRange(e[0])
        }
    }
    q.getRangeAt = function(e) {
        if (0 > e || e >= this.rangeCount) throw new N("INDEX_SIZE_ERR");
        return this._ranges[e]
    };
    var U;
    if (_) U = function(t) {
        var n;
        e.isSelectionValid(t.win) ? n = t.docSelection.createRange() : (n = x.getBody(t.win.document).createTextRange(), n.collapse(!0)), t.docSelection.type == A ? p(t) : u(n) ? c(t, n) : o(t)
    };
    else if (C.isHostMethod(j, "getRangeAt") && "number" == typeof j.rangeCount) U = function(t) {
        if ($ && G && t.docSelection.type == A) p(t);
        else if (t._ranges.length = t.rangeCount = t.nativeSelection.rangeCount, t.rangeCount) {
            for (var n = 0, i = t.rangeCount; i > n; ++n) t._ranges[n] = new e.WrappedRange(t.nativeSelection.getRangeAt(n));
            a(t, t._ranges[t.rangeCount - 1], Y(t.nativeSelection)), t.isCollapsed = b(t)
        } else o(t)
    };
    else {
        if (!I || typeof j.isCollapsed != w || typeof M.collapsed != w || !e.features.implementsDomRange) return t.fail("No means of obtaining a Range or TextRange from the user's selection was found"), !1;
        U = function(e) {
            var t, n = e.nativeSelection;
            n.anchorNode ? (t = B(n, 0), e._ranges = [t], e.rangeCount = 1, r(e), e.isCollapsed = b(e)) : o(e)
        }
    }
    q.refresh = function(e) {
        var t = e ? this._ranges.slice(0) : null;
        if (U(this), e) {
            var n = t.length;
            if (n != this._ranges.length) return !1;
            for (; n--;)
                if (!k.rangesEqual(t[n], this._ranges[n])) return !1;
            return !0
        }
    };
    var W = function(e, t) {
        var n = e.getAllRanges(),
            i = !1;
        e.removeAllRanges();
        for (var a = 0, r = n.length; r > a; ++a) i || t !== n[a] ? e.addRange(n[a]) : i = !0;
        e.rangeCount || o(e)
    };
    q.removeRange = $ ? function(e) {
        if (this.docSelection.type == A) {
            for (var t, n = this.docSelection.createRange(), i = l(e), a = x.getDocument(n.item(0)), r = x.getBody(a).createControlRange(), o = !1, s = 0, d = n.length; d > s; ++s) t = n.item(s), t !== i || o ? r.add(n.item(s)) : o = !0;
            r.select(), p(this)
        } else W(this, e)
    } : function(e) {
        W(this, e)
    };
    var Y;
    !_ && I && e.features.implementsDomRange ? (Y = function(e) {
        var t = !1;
        return e.anchorNode && (t = 1 == x.comparePoints(e.anchorNode, e.anchorOffset, e.focusNode, e.focusOffset)), t
    }, q.isBackwards = function() {
        return Y(this)
    }) : Y = q.isBackwards = function() {
        return !1
    }, q.toString = function() {
        for (var e = [], t = 0, n = this.rangeCount; n > t; ++t) e[t] = "" + this._ranges[t];
        return e.join("")
    }, q.collapse = function(t, n) {
        g(this, t);
        var i = e.createRange(x.getDocument(t));
        i.collapseToPoint(t, n), this.removeAllRanges(), this.addRange(i), this.isCollapsed = !0
    }, q.collapseToStart = function() {
        if (!this.rangeCount) throw new N("INVALID_STATE_ERR");
        var e = this._ranges[0];
        this.collapse(e.startContainer, e.startOffset)
    }, q.collapseToEnd = function() {
        if (!this.rangeCount) throw new N("INVALID_STATE_ERR");
        var e = this._ranges[this.rangeCount - 1];
        this.collapse(e.endContainer, e.endOffset)
    }, q.selectAllChildren = function(t) {
        g(this, t);
        var n = e.createRange(x.getDocument(t));
        n.selectNodeContents(t), this.removeAllRanges(), this.addRange(n)
    }, q.deleteFromDocument = function() {
        if ($ && G && this.docSelection.type == A) {
            for (var e, t = this.docSelection.createRange(); t.length;) e = t.item(0), t.remove(e), e.parentNode.removeChild(e);
            this.refresh()
        } else if (this.rangeCount) {
            var n = this.getAllRanges();
            this.removeAllRanges();
            for (var i = 0, a = n.length; a > i; ++i) n[i].deleteContents();
            this.addRange(n[a - 1])
        }
    }, q.getAllRanges = function() {
        return this._ranges.slice(0)
    }, q.setSingleRange = function(e) {
        this.setRanges([e])
    }, q.containsNode = function(e, t) {
        for (var n = 0, i = this._ranges.length; i > n; ++n)
            if (this._ranges[n].containsNode(e, t)) return !0;
        return !1
    }, q.toHtml = function() {
        var e = "";
        if (this.rangeCount) {
            for (var t = k.getRangeDocument(this._ranges[0]).createElement("div"), n = 0, i = this._ranges.length; i > n; ++n) t.appendChild(this._ranges[n].cloneContents());
            e = t.innerHTML
        }
        return e
    }, q.getName = function() {
        return "WrappedSelection"
    }, q.inspect = function() {
        return v(this)
    }, q.detach = function() {
        this.win[T] = null, this.win = this.anchorNode = this.focusNode = null
    }, h.inspect = v, e.Selection = h, e.selectionPrototype = q, e.addCreateMissingNativeApiListener(function(t) {
        "undefined" == typeof t.getSelection && (t.getSelection = function() {
            return e.getSelection(this)
        }), t = null
    })
});
var Base = function() {};
Base.extend = function(e, t) {
        var n = Base.prototype.extend;
        Base._prototyping = !0;
        var i = new this;
        n.call(i, e), i.base = function() {}, delete Base._prototyping;
        var a = i.constructor,
            r = i.constructor = function() {
                if (!Base._prototyping)
                    if (this._constructing || this.constructor == r) this._constructing = !0, a.apply(this, arguments), delete this._constructing;
                    else if (null != arguments[0]) return (arguments[0].extend || n).call(arguments[0], i)
            };
        return r.ancestor = this, r.extend = this.extend, r.forEach = this.forEach, r.implement = this.implement, r.prototype = i, r.toString = this.toString, r.valueOf = function(e) {
            return "object" == e ? r : a.valueOf()
        }, n.call(r, t), "function" == typeof r.init && r.init(), r
    }, Base.prototype = {
        extend: function(e, t) {
            if (arguments.length > 1) {
                var n = this[e];
                if (n && "function" == typeof t && (!n.valueOf || n.valueOf() != t.valueOf()) && /\bbase\b/.test(t)) {
                    var i = t.valueOf();
                    t = function() {
                        var e = this.base || Base.prototype.base;
                        this.base = n;
                        var t = i.apply(this, arguments);
                        return this.base = e, t
                    }, t.valueOf = function(e) {
                        return "object" == e ? t : i
                    }, t.toString = Base.toString
                }
                this[e] = t
            } else if (e) {
                var a = Base.prototype.extend;
                Base._prototyping || "function" == typeof this || (a = this.extend || a);
                for (var r = {
                        toSource: null
                    }, o = ["constructor", "toString", "valueOf"], s = Base._prototyping ? 0 : 1; d = o[s++];) e[d] != r[d] && a.call(this, d, e[d]);
                for (var d in e) r[d] || a.call(this, d, e[d])
            }
            return this
        }
    }, Base = Base.extend({
        constructor: function() {
            this.extend(arguments[0])
        }
    }, {
        ancestor: Object,
        version: "1.1",
        forEach: function(e, t, n) {
            for (var i in e) void 0 === this.prototype[i] && t.call(n, e[i], i, e)
        },
        implement: function() {
            for (var e = 0; e < arguments.length; e++) "function" == typeof arguments[e] ? arguments[e](this.prototype) : this.prototype.extend(arguments[e]);
            return this
        },
        toString: function() {
            return String(this.valueOf())
        }
    }), wysihtml5.browser = function() {
        function e(e) {
            return (/ipad|iphone|ipod/.test(e) && e.match(/ os (\d+).+? like mac os x/) || [, 0])[1]
        }
        var t = navigator.userAgent,
            n = document.createElement("div"),
            i = -1 !== t.indexOf("MSIE") && -1 === t.indexOf("Opera"),
            a = -1 !== t.indexOf("Gecko") && -1 === t.indexOf("KHTML"),
            r = -1 !== t.indexOf("AppleWebKit/"),
            o = -1 !== t.indexOf("Chrome/"),
            s = -1 !== t.indexOf("Opera/");
        return {
            USER_AGENT: t,
            supported: function() {
                var t = this.USER_AGENT.toLowerCase(),
                    i = "contentEditable" in n,
                    a = document.execCommand && document.queryCommandSupported && document.queryCommandState,
                    r = document.querySelector && document.querySelectorAll,
                    o = this.isIos() && e(t) < 5 || -1 !== t.indexOf("opera mobi") || -1 !== t.indexOf("hpwos/");
                return i && a && r && !o
            },
            isTouchDevice: function() {
                return this.supportsEvent("touchmove")
            },
            isIos: function() {
                var e = this.USER_AGENT.toLowerCase();
                return -1 !== e.indexOf("webkit") && -1 !== e.indexOf("mobile")
            },
            supportsSandboxedIframes: function() {
                return i
            },
            throwsMixedContentWarningWhenIframeSrcIsEmpty: function() {
                return !("querySelector" in document)
            },
            displaysCaretInEmptyContentEditableCorrectly: function() {
                return !a
            },
            hasCurrentStyleProperty: function() {
                return "currentStyle" in n
            },
            insertsLineBreaksOnReturn: function() {
                return a
            },
            supportsPlaceholderAttributeOn: function(e) {
                return "placeholder" in e
            },
            supportsEvent: function(e) {
                return "on" + e in n || function() {
                    return n.setAttribute("on" + e, "return;"), "function" == typeof n["on" + e]
                }()
            },
            supportsEventsInIframeCorrectly: function() {
                return !s
            },
            firesOnDropOnlyWhenOnDragOverIsCancelled: function() {
                return r || a
            },
            supportsDataTransfer: function() {
                try {
                    return r && (window.Clipboard || window.DataTransfer).prototype.getData
                } catch (e) {
                    return !1
                }
            },
            supportsHTML5Tags: function(e) {
                var t = e.createElement("div"),
                    n = "<article>foo</article>";
                return t.innerHTML = n, t.innerHTML.toLowerCase() === n
            },
            supportsCommand: function() {
                var e = {
                        formatBlock: i,
                        insertUnorderedList: i || s || r,
                        insertOrderedList: i || s || r
                    },
                    t = {
                        insertHTML: a
                    };
                return function(n, i) {
                    var a = e[i];
                    if (!a) {
                        try {
                            return n.queryCommandSupported(i)
                        } catch (r) {}
                        try {
                            return n.queryCommandEnabled(i)
                        } catch (o) {
                            return !!t[i]
                        }
                    }
                    return !1
                }
            }(),
            doesAutoLinkingInContentEditable: function() {
                return i
            },
            canDisableAutoLinking: function() {
                return this.supportsCommand(document, "AutoUrlDetect")
            },
            clearsContentEditableCorrectly: function() {
                return a || s || r
            },
            supportsGetAttributeCorrectly: function() {
                var e = document.createElement("td");
                return "1" != e.getAttribute("rowspan")
            },
            canSelectImagesInContentEditable: function() {
                return a || i || s
            },
            clearsListsInContentEditableCorrectly: function() {
                return a || i || r
            },
            autoScrollsToCaret: function() {
                return !r
            },
            autoClosesUnclosedTags: function() {
                var e, t, i = n.cloneNode(!1);
                return i.innerHTML = "<p><div></div>", t = i.innerHTML.toLowerCase(), e = "<p></p><div></div>" === t || "<p><div></div></p>" === t, this.autoClosesUnclosedTags = function() {
                    return e
                }, e
            },
            supportsNativeGetElementsByClassName: function() {
                return -1 !== String(document.getElementsByClassName).indexOf("[native code]")
            },
            supportsSelectionModify: function() {
                return "getSelection" in window && "modify" in window.getSelection()
            },
            supportsClassList: function() {
                return "classList" in n
            },
            needsSpaceAfterLineBreak: function() {
                return s
            },
            supportsSpeechApiOn: function(e) {
                var n = t.match(/Chrome\/(\d+)/) || [, 0];
                return n[1] >= 11 && ("onwebkitspeechchange" in e || "speech" in e)
            },
            crashesWhenDefineProperty: function(e) {
                return i && ("XMLHttpRequest" === e || "XDomainRequest" === e)
            },
            doesAsyncFocus: function() {
                return i
            },
            hasProblemsSettingCaretAfterImg: function() {
                return i
            },
            hasUndoInContextMenu: function() {
                return a || o || s
            }
        }
    }(), wysihtml5.lang.array = function(e) {
        return {
            contains: function(t) {
                if (e.indexOf) return -1 !== e.indexOf(t);
                for (var n = 0, i = e.length; i > n; n++)
                    if (e[n] === t) return !0;
                return !1
            },
            without: function(t) {
                t = wysihtml5.lang.array(t);
                for (var n = [], i = 0, a = e.length; a > i; i++) t.contains(e[i]) || n.push(e[i]);
                return n
            },
            get: function() {
                for (var t = 0, n = e.length, i = []; n > t; t++) i.push(e[t]);
                return i
            }
        }
    }, wysihtml5.lang.Dispatcher = Base.extend({
        observe: function(e, t) {
            return this.events = this.events || {}, this.events[e] = this.events[e] || [], this.events[e].push(t), this
        },
        on: function() {
            return this.observe.apply(this, wysihtml5.lang.array(arguments).get())
        },
        fire: function(e, t) {
            this.events = this.events || {};
            for (var n = this.events[e] || [], i = 0; i < n.length; i++) n[i].call(this, t);
            return this
        },
        stopObserving: function(e, t) {
            this.events = this.events || {};
            var n, i, a = 0;
            if (e) {
                for (n = this.events[e] || [], i = []; a < n.length; a++) n[a] !== t && t && i.push(n[a]);
                this.events[e] = i
            } else this.events = {};
            return this
        }
    }), wysihtml5.lang.object = function(e) {
        return {
            merge: function(t) {
                for (var n in t) e[n] = t[n];
                return this
            },
            get: function() {
                return e
            },
            clone: function() {
                var t, n = {};
                for (t in e) n[t] = e[t];
                return n
            },
            isArray: function() {
                return "[object Array]" === Object.prototype.toString.call(e)
            }
        }
    },
    function() {
        var e = /^\s+/,
            t = /\s+$/;
        wysihtml5.lang.string = function(n) {
            return n = String(n), {
                trim: function() {
                    return n.replace(e, "").replace(t, "")
                },
                interpolate: function(e) {
                    for (var t in e) n = this.replace("#{" + t + "}").by(e[t]);
                    return n
                },
                replace: function(e) {
                    return {
                        by: function(t) {
                            return n.split(e).join(t)
                        }
                    }
                }
            }
        }
    }(),
    function(e) {
        function t(e) {
            return r(e) ? e : (e === e.ownerDocument.documentElement && (e = e.ownerDocument.body), o(e))
        }

        function n(e) {
            return e.replace(d, function(e, t) {
                var n = (t.match(l) || [])[1] || "",
                    i = c[n];
                t = t.replace(l, ""), t.split(i).length > t.split(n).length && (t += n, n = "");
                var a = t,
                    r = t;
                return t.length > u && (r = r.substr(0, u) + "..."), "www." === a.substr(0, 4) && (a = "http://" + a), '<a href="' + a + '">' + r + "</a>" + n
            })
        }

        function i(e) {
            var t = e._wysihtml5_tempElement;
            return t || (t = e._wysihtml5_tempElement = e.createElement("div")), t
        }

        function a(e) {
            var t = e.parentNode,
                a = i(t.ownerDocument);
            for (a.innerHTML = "<span></span>" + n(e.data), a.removeChild(a.firstChild); a.firstChild;) t.insertBefore(a.firstChild, e);
            t.removeChild(e)
        }

        function r(e) {
            for (var t; e.parentNode;) {
                if (e = e.parentNode, t = e.nodeName, s.contains(t)) return !0;
                if ("body" === t) return !1
            }
            return !1
        }

        function o(t) {
            if (!s.contains(t.nodeName)) {
                if (t.nodeType === e.TEXT_NODE && t.data.match(d)) return a(t), void 0;
                for (var n = e.lang.array(t.childNodes).get(), i = n.length, r = 0; i > r; r++) o(n[r]);
                return t
            }
        }
        var s = e.lang.array(["CODE", "PRE", "A", "SCRIPT", "HEAD", "TITLE", "STYLE"]),
            d = /((https?:\/\/|www\.)[^\s<]{3,})/gi,
            l = /([^\w\/\-](,?))$/i,
            u = 100,
            c = {
                ")": "(",
                "]": "[",
                "}": "{"
            };
        e.dom.autoLink = t, e.dom.autoLink.URL_REG_EXP = d
    }(wysihtml5),
    function(e) {
        var t = e.browser.supportsClassList(),
            n = e.dom;
        n.addClass = function(e, i) {
            return t ? e.classList.add(i) : (n.hasClass(e, i) || (e.className += " " + i), void 0)
        }, n.removeClass = function(e, n) {
            return t ? e.classList.remove(n) : (e.className = e.className.replace(new RegExp("(^|\\s+)" + n + "(\\s+|$)"), " "), void 0)
        }, n.hasClass = function(e, n) {
            if (t) return e.classList.contains(n);
            var i = e.className;
            return i.length > 0 && (i == n || new RegExp("(^|\\s)" + n + "(\\s|$)").test(i))
        }
    }(wysihtml5), wysihtml5.dom.contains = function() {
        var e = document.documentElement;
        return e.contains ? function(e, t) {
            return t.nodeType !== wysihtml5.ELEMENT_NODE && (t = t.parentNode), e !== t && e.contains(t)
        } : e.compareDocumentPosition ? function(e, t) {
            return !!(16 & e.compareDocumentPosition(t))
        } : void 0
    }(), wysihtml5.dom.convertToList = function() {
        function e(e, t) {
            var n = e.createElement("li");
            return t.appendChild(n), n
        }

        function t(e, t) {
            return e.createElement(t)
        }

        function n(n, i) {
            if ("UL" === n.nodeName || "OL" === n.nodeName || "MENU" === n.nodeName) return n;
            var a, r, o, s, d, l, u, c, p, f = n.ownerDocument,
                h = t(f, i),
                m = n.querySelectorAll("br"),
                g = m.length;
            for (p = 0; g > p; p++)
                for (s = m[p];
                    (d = s.parentNode) && d !== n && d.lastChild === s;) {
                    if ("block" === wysihtml5.dom.getStyle("display").from(d)) {
                        d.removeChild(s);
                        break
                    }
                    wysihtml5.dom.insert(s).after(s.parentNode)
                }
            for (a = wysihtml5.lang.array(n.childNodes).get(), r = a.length, p = 0; r > p; p++) c = c || e(f, h), o = a[p], l = "block" === wysihtml5.dom.getStyle("display").from(o), u = "BR" === o.nodeName, l ? (c = c.firstChild ? e(f, h) : c, c.appendChild(o), c = null) : u ? c = c.firstChild ? null : c : c.appendChild(o);
            return n.parentNode.replaceChild(h, n), h
        }
        return n
    }(), wysihtml5.dom.copyAttributes = function(e) {
        return {
            from: function(t) {
                return {
                    to: function(n) {
                        for (var i, a = 0, r = e.length; r > a; a++) i = e[a], "undefined" != typeof t[i] && "" !== t[i] && (n[i] = t[i]);
                        return {
                            andTo: arguments.callee
                        }
                    }
                }
            }
        }
    },
    function(e) {
        var t = ["-webkit-box-sizing", "-moz-box-sizing", "-ms-box-sizing", "box-sizing"],
            n = function(t) {
                return i(t) ? parseInt(e.getStyle("width").from(t), 10) < t.offsetWidth : !1
            },
            i = function(n) {
                for (var i = 0, a = t.length; a > i; i++)
                    if ("border-box" === e.getStyle(t[i]).from(n)) return t[i]
            };
        e.copyStyles = function(i) {
            return {
                from: function(a) {
                    n(a) && (i = wysihtml5.lang.array(i).without(t));
                    for (var r, o = "", s = i.length, d = 0; s > d; d++) r = i[d], o += r + ":" + e.getStyle(r).from(a) + ";";
                    return {
                        to: function(t) {
                            return e.setStyles(o).on(t), {
                                andTo: arguments.callee
                            }
                        }
                    }
                }
            }
        }
    }(wysihtml5.dom),
    function(e) {
        e.dom.delegate = function(t, n, i, a) {
            return e.dom.observe(t, i, function(i) {
                for (var r = i.target, o = e.lang.array(t.querySelectorAll(n)); r && r !== t;) {
                    if (o.contains(r)) {
                        a.call(r, i);
                        break
                    }
                    r = r.parentNode
                }
            })
        }
    }(wysihtml5), wysihtml5.dom.getAsDom = function() {
        var e = function(e, t) {
                var n = t.createElement("div");
                n.style.display = "none", t.body.appendChild(n);
                try {
                    n.innerHTML = e
                } catch (i) {}
                return t.body.removeChild(n), n
            },
            t = function(e) {
                if (!e._wysihtml5_supportsHTML5Tags) {
                    for (var t = 0, i = n.length; i > t; t++) e.createElement(n[t]);
                    e._wysihtml5_supportsHTML5Tags = !0
                }
            },
            n = ["abbr", "article", "aside", "audio", "bdi", "canvas", "command", "datalist", "details", "figcaption", "figure", "footer", "header", "hgroup", "keygen", "mark", "meter", "nav", "output", "progress", "rp", "rt", "ruby", "svg", "section", "source", "summary", "time", "track", "video", "wbr"];
        return function(n, i) {
            i = i || document;
            var a;
            return "object" == typeof n && n.nodeType ? (a = i.createElement("div"), a.appendChild(n)) : wysihtml5.browser.supportsHTML5Tags(i) ? (a = i.createElement("div"), a.innerHTML = n) : (t(i), a = e(n, i)), a
        }
    }(), wysihtml5.dom.getParentElement = function() {
        function e(e, t) {
            return t && t.length ? "string" == typeof t ? e === t : wysihtml5.lang.array(t).contains(e) : !0
        }

        function t(e) {
            return e.nodeType === wysihtml5.ELEMENT_NODE
        }

        function n(e, t, n) {
            var i = (e.className || "").match(n) || [];
            return t ? i[i.length - 1] === t : !!i.length
        }

        function i(t, n, i) {
            for (; i-- && t && "BODY" !== t.nodeName;) {
                if (e(t.nodeName, n)) return t;
                t = t.parentNode
            }
            return null
        }

        function a(i, a, r, o, s) {
            for (; s-- && i && "BODY" !== i.nodeName;) {
                if (t(i) && e(i.nodeName, a) && n(i, r, o)) return i;
                i = i.parentNode
            }
            return null
        }
        return function(e, t, n) {
            return n = n || 50, t.className || t.classRegExp ? a(e, t.nodeName, t.className, t.classRegExp, n) : i(e, t.nodeName, n)
        }
    }(), wysihtml5.dom.getStyle = function() {
        function e(e) {
            return e.replace(n, function(e) {
                return e.charAt(1).toUpperCase()
            })
        }
        var t = {
                "float": "styleFloat" in document.createElement("div").style ? "styleFloat" : "cssFloat"
            },
            n = /\-[a-z]/g;
        return function(n) {
            return {
                from: function(i) {
                    if (i.nodeType === wysihtml5.ELEMENT_NODE) {
                        var a = i.ownerDocument,
                            r = t[n] || e(n),
                            o = i.style,
                            s = i.currentStyle,
                            d = o[r];
                        if (d) return d;
                        if (s) try {
                            return s[r]
                        } catch (l) {}
                        var u, c, p = a.defaultView || a.parentWindow,
                            f = ("height" === n || "width" === n) && "TEXTAREA" === i.nodeName;
                        return p.getComputedStyle ? (f && (u = o.overflow, o.overflow = "hidden"), c = p.getComputedStyle(i, null).getPropertyValue(n), f && (o.overflow = u || ""), c) : void 0
                    }
                }
            }
        }
    }(), wysihtml5.dom.hasElementWithTagName = function() {
        function e(e) {
            return e._wysihtml5_identifier || (e._wysihtml5_identifier = n++)
        }
        var t = {},
            n = 1;
        return function(n, i) {
            var a = e(n) + ":" + i,
                r = t[a];
            return r || (r = t[a] = n.getElementsByTagName(i)), r.length > 0
        }
    }(),
    function(e) {
        function t(e) {
            return e._wysihtml5_identifier || (e._wysihtml5_identifier = i++)
        }
        var n = {},
            i = 1;
        e.dom.hasElementWithClassName = function(i, a) {
            if (!e.browser.supportsNativeGetElementsByClassName()) return !!i.querySelector("." + a);
            var r = t(i) + ":" + a,
                o = n[r];
            return o || (o = n[r] = i.getElementsByClassName(a)), o.length > 0
        }
    }(wysihtml5), wysihtml5.dom.insert = function(e) {
        return {
            after: function(t) {
                t.parentNode.insertBefore(e, t.nextSibling)
            },
            before: function(t) {
                t.parentNode.insertBefore(e, t)
            },
            into: function(t) {
                t.appendChild(e)
            }
        }
    }, wysihtml5.dom.insertCSS = function(e) {
        return e = e.join("\n"), {
            into: function(t) {
                var n = t.head || t.getElementsByTagName("head")[0],
                    i = t.createElement("style");
                i.type = "text/css", i.styleSheet ? i.styleSheet.cssText = e : i.appendChild(t.createTextNode(e)), n && n.appendChild(i)
            }
        }
    }, wysihtml5.dom.observe = function(e, t, n) {
        t = "string" == typeof t ? [t] : t;
        for (var i, a, r = 0, o = t.length; o > r; r++) a = t[r], e.addEventListener ? e.addEventListener(a, n, !1) : (i = function(t) {
            "target" in t || (t.target = t.srcElement), t.preventDefault = t.preventDefault || function() {
                this.returnValue = !1
            }, t.stopPropagation = t.stopPropagation || function() {
                this.cancelBubble = !0
            }, n.call(e, t)
        }, e.attachEvent("on" + a, i));
        return {
            stop: function() {
                for (var a, r = 0, o = t.length; o > r; r++) a = t[r], e.removeEventListener ? e.removeEventListener(a, n, !1) : e.detachEvent("on" + a, i)
            }
        }
    }, wysihtml5.dom.parse = function() {
        function e(e, n, i, a) {
            wysihtml5.lang.object(c).merge(u).merge(n).get(), i = i || e.ownerDocument || document;
            var r, o, s, d = i.createDocumentFragment(),
                l = "string" == typeof e;
            for (r = l ? wysihtml5.dom.getAsDom(e, i) : e; r.firstChild;) s = r.firstChild, r.removeChild(s), o = t(s, a), o && d.appendChild(o);
            return r.innerHTML = "", r.appendChild(d), l ? wysihtml5.quirks.getCorrectInnerHTML(r) : r
        }

        function t(e, n) {
            var i, a = e.nodeType,
                r = e.childNodes,
                o = r.length,
                l = s[a],
                u = 0;
            if (i = l && l(e), !i) return null;
            for (u = 0; o > u; u++) newChild = t(r[u], n), newChild && i.appendChild(newChild);
            return n && i.childNodes.length <= 1 && i.nodeName.toLowerCase() === d && !i.attributes.length ? i.firstChild : i
        }

        function n(e) {
            var t, n, a = c.tags,
                r = e.nodeName.toLowerCase(),
                o = e.scopeName;
            if (e._wysihtml5) return null;
            if (e._wysihtml5 = 1, "wysihtml5-temp" === e.className) return null;
            if (o && "HTML" != o && (r = o + ":" + r), "outerHTML" in e && (wysihtml5.browser.autoClosesUnclosedTags() || "P" !== e.nodeName || "</p>" === e.outerHTML.slice(-4).toLowerCase() || (r = "div")), r in a) {
                if (t = a[r], !t || t.remove) return null;
                t = "string" == typeof t ? {
                    rename_tag: t
                } : t
            } else {
                if (!e.firstChild) return null;
                t = {
                    rename_tag: d
                }
            }
            return n = e.ownerDocument.createElement(t.rename_tag || r), i(e, n, t), e = null, n
        }

        function i(e, t, n) {
            var i, r, o, s, d, u, p, m = {},
                g = n.set_class,
                v = n.add_class,
                y = n.set_attributes,
                b = n.check_attributes,
                w = c.classes,
                T = 0,
                x = [],
                C = [],
                k = [],
                E = [];
            if (y && (m = wysihtml5.lang.object(y).clone()), b)
                for (d in b) p = f[b[d]], p && (u = p(a(e, d)), "string" == typeof u && (m[d] = u));
            if (g && x.push(g), v)
                for (d in v) p = h[v[d]], p && (s = p(a(e, d)), "string" == typeof s && x.push(s));
            for (w["_wysihtml5-temp-placeholder"] = 1, E = e.getAttribute("class"), E && (x = x.concat(E.split(l))), i = x.length; i > T; T++) o = x[T], w[o] && C.push(o);
            for (r = C.length; r--;) o = C[r], wysihtml5.lang.array(k).contains(o) || k.unshift(o);
            k.length && (m["class"] = k.join(" "));
            for (d in m) try {
                t.setAttribute(d, m[d])
            } catch (N) {}
            m.src && ("undefined" != typeof m.width && t.setAttribute("width", m.width), "undefined" != typeof m.height && t.setAttribute("height", m.height))
        }

        function a(e, t) {
            t = t.toLowerCase();
            var n = e.nodeName;
            if ("IMG" == n && "src" == t && r(e) === !0) return e.src;
            if (p && "outerHTML" in e) {
                var i = e.outerHTML.toLowerCase(),
                    a = -1 != i.indexOf(" " + t + "=");
                return a ? e.getAttribute(t) : null
            }
            return e.getAttribute(t)
        }

        function r(e) {
            try {
                return e.complete && !e.mozMatchesSelector(":-moz-broken")
            } catch (t) {
                if (e.complete && "complete" === e.readyState) return !0
            }
        }

        function o(e) {
            return e.ownerDocument.createTextNode(e.data)
        }
        var s = {
                1: n,
                3: o
            },
            d = "span",
            l = /\s+/,
            u = {
                tags: {},
                classes: {}
            },
            c = {},
            p = !wysihtml5.browser.supportsGetAttributeCorrectly(),
            f = {
                url: function() {
                    var e = /^https?:\/\//i;
                    return function(t) {
                        return t && t.match(e) ? t.replace(e, function(e) {
                            return e.toLowerCase()
                        }) : null
                    }
                }(),
                alt: function() {
                    var e = /[^ a-z0-9_\-]/gi;
                    return function(t) {
                        return t ? t.replace(e, "") : ""
                    }
                }(),
                numbers: function() {
                    var e = /\D/g;
                    return function(t) {
                        return t = (t || "").replace(e, ""), t || null
                    }
                }()
            },
            h = {
                align_img: function() {
                    var e = {
                        left: "wysiwyg-float-left",
                        right: "wysiwyg-float-right"
                    };
                    return function(t) {
                        return e[String(t).toLowerCase()]
                    }
                }(),
                align_text: function() {
                    var e = {
                        left: "wysiwyg-text-align-left",
                        right: "wysiwyg-text-align-right",
                        center: "wysiwyg-text-align-center",
                        justify: "wysiwyg-text-align-justify"
                    };
                    return function(t) {
                        return e[String(t).toLowerCase()]
                    }
                }(),
                clear_br: function() {
                    var e = {
                        left: "wysiwyg-clear-left",
                        right: "wysiwyg-clear-right",
                        both: "wysiwyg-clear-both",
                        all: "wysiwyg-clear-both"
                    };
                    return function(t) {
                        return e[String(t).toLowerCase()]
                    }
                }(),
                size_font: function() {
                    var e = {
                        1: "wysiwyg-font-size-xx-small",
                        2: "wysiwyg-font-size-small",
                        3: "wysiwyg-font-size-medium",
                        4: "wysiwyg-font-size-large",
                        5: "wysiwyg-font-size-x-large",
                        6: "wysiwyg-font-size-xx-large",
                        7: "wysiwyg-font-size-xx-large",
                        "-": "wysiwyg-font-size-smaller",
                        "+": "wysiwyg-font-size-larger"
                    };
                    return function(t) {
                        return e[String(t).charAt(0)]
                    }
                }()
            };
        return e
    }(), wysihtml5.dom.removeEmptyTextNodes = function(e) {
        for (var t, n = wysihtml5.lang.array(e.childNodes).get(), i = n.length, a = 0; i > a; a++) t = n[a], t.nodeType === wysihtml5.TEXT_NODE && "" === t.data && t.parentNode.removeChild(t)
    }, wysihtml5.dom.renameElement = function(e, t) {
        for (var n, i = e.ownerDocument.createElement(t); n = e.firstChild;) i.appendChild(n);
        return wysihtml5.dom.copyAttributes(["align", "className"]).from(e).to(i), e.parentNode.replaceChild(i, e), i
    }, wysihtml5.dom.replaceWithChildNodes = function(e) {
        if (e.parentNode) {
            if (!e.firstChild) return e.parentNode.removeChild(e), void 0;
            for (var t = e.ownerDocument.createDocumentFragment(); e.firstChild;) t.appendChild(e.firstChild);
            e.parentNode.replaceChild(t, e), e = t = null
        }
    },
    function(e) {
        function t(t) {
            return "block" === e.getStyle("display").from(t)
        }

        function n(e) {
            return "BR" === e.nodeName
        }

        function i(e) {
            var t = e.ownerDocument.createElement("br");
            e.appendChild(t)
        }

        function a(e) {
            if ("MENU" === e.nodeName || "UL" === e.nodeName || "OL" === e.nodeName) {
                var a, r, o, s, d, l = e.ownerDocument,
                    u = l.createDocumentFragment(),
                    c = e.previousElementSibling || e.previousSibling;
                for (c && !t(c) && i(u); d = e.firstChild;) {
                    for (r = d.lastChild; a = d.firstChild;) o = a === r, s = o && !t(a) && !n(a), u.appendChild(a), s && i(u);
                    d.parentNode.removeChild(d)
                }
                e.parentNode.replaceChild(u, e)
            }
        }
        e.resolveList = a
    }(wysihtml5.dom),
    function(e) {
        var t = document,
            n = ["parent", "top", "opener", "frameElement", "frames", "localStorage", "globalStorage", "sessionStorage", "indexedDB"],
            i = ["open", "close", "openDialog", "showModalDialog", "alert", "confirm", "prompt", "openDatabase", "postMessage", "XMLHttpRequest", "XDomainRequest"],
            a = ["referrer", "write", "open", "close"];
        e.dom.Sandbox = Base.extend({
            constructor: function(t, n) {
                this.callback = t || e.EMPTY_FUNCTION, this.config = e.lang.object({}).merge(n).get(), this.iframe = this._createIframe()
            },
            insertInto: function(e) {
                "string" == typeof e && (e = t.getElementById(e)), e.appendChild(this.iframe)
            },
            getIframe: function() {
                return this.iframe
            },
            getWindow: function() {
                this._readyError()
            },
            getDocument: function() {
                this._readyError()
            },
            destroy: function() {
                var e = this.getIframe();
                e.parentNode.removeChild(e)
            },
            _readyError: function() {
                throw new Error("wysihtml5.Sandbox: Sandbox iframe isn't loaded yet")
            },
            _createIframe: function() {
                var n = this,
                    i = t.createElement("iframe");
                return i.className = "wysihtml5-sandbox", e.dom.setAttributes({
                    security: "restricted",
                    allowtransparency: "true",
                    frameborder: 0,
                    width: 0,
                    height: 0,
                    marginwidth: 0,
                    marginheight: 0
                }).on(i), e.browser.throwsMixedContentWarningWhenIframeSrcIsEmpty() && (i.src = "javascript:'<html></html>'"), i.onload = function() {
                    i.onreadystatechange = i.onload = null, n._onLoadIframe(i)
                }, i.onreadystatechange = function() {
                    /loaded|complete/.test(i.readyState) && (i.onreadystatechange = i.onload = null, n._onLoadIframe(i))
                }, i
            },
            _onLoadIframe: function(r) {
                if (e.dom.contains(t.documentElement, r)) {
                    var o = this,
                        s = r.contentWindow,
                        d = r.contentWindow.document,
                        l = t.characterSet || t.charset || "utf-8",
                        u = this._getHtml({
                            charset: l,
                            stylesheets: this.config.stylesheets
                        });
                    if (d.open("text/html", "replace"), d.write(u), d.close(), this.getWindow = function() {
                            return r.contentWindow
                        }, this.getDocument = function() {
                            return r.contentWindow.document
                        }, s.onerror = function(e, t, n) {
                            throw new Error("wysihtml5.Sandbox: " + e, t, n)
                        }, !e.browser.supportsSandboxedIframes()) {
                        var c, p;
                        for (c = 0, p = n.length; p > c; c++) this._unset(s, n[c]);
                        for (c = 0, p = i.length; p > c; c++) this._unset(s, i[c], e.EMPTY_FUNCTION);
                        for (c = 0, p = a.length; p > c; c++) this._unset(d, a[c]);
                        this._unset(d, "cookie", "", !0)
                    }
                    this.loaded = !0, setTimeout(function() {
                        o.callback(o)
                    }, 0)
                }
            },
            _getHtml: function(t) {
                var n, i = t.stylesheets,
                    a = "",
                    r = 0;
                if (i = "string" == typeof i ? [i] : i)
                    for (n = i.length; n > r; r++) a += '<link rel="stylesheet" href="' + i[r] + '">';
                return t.stylesheets = a, e.lang.string('<!DOCTYPE html><html><head><meta charset="#{charset}">#{stylesheets}</head><body></body></html>').interpolate(t)
            },
            _unset: function(t, n, i, a) {
                try {
                    t[n] = i
                } catch (r) {}
                try {
                    t.__defineGetter__(n, function() {
                        return i
                    })
                } catch (r) {}
                if (a) try {
                    t.__defineSetter__(n, function() {})
                } catch (r) {}
                if (!e.browser.crashesWhenDefineProperty(n)) try {
                    var o = {
                        get: function() {
                            return i
                        }
                    };
                    a && (o.set = function() {}), Object.defineProperty(t, n, o)
                } catch (r) {}
            }
        })
    }(wysihtml5),
    function() {
        var e = {
            className: "class"
        };
        wysihtml5.dom.setAttributes = function(t) {
            return {
                on: function(n) {
                    for (var i in t) n.setAttribute(e[i] || i, t[i])
                }
            }
        }
    }(), wysihtml5.dom.setStyles = function(e) {
        return {
            on: function(t) {
                var n = t.style;
                if ("string" == typeof e) return n.cssText += ";" + e, void 0;
                for (var i in e) "float" === i ? (n.cssFloat = e[i], n.styleFloat = e[i]) : n[i] = e[i]
            }
        }
    },
    function(e) {
        e.simulatePlaceholder = function(t, n, i) {
            var a = "placeholder",
                r = function() {
                    n.hasPlaceholderSet() && n.clear(), e.removeClass(n.element, a)
                },
                o = function() {
                    n.isEmpty() && (n.setValue(i), e.addClass(n.element, a))
                };
            t.observe("set_placeholder", o).observe("unset_placeholder", r).observe("focus:composer", r).observe("paste:composer", r).observe("blur:composer", o), o()
        }
    }(wysihtml5.dom),
    function(e) {
        var t = document.documentElement;
        "textContent" in t ? (e.setTextContent = function(e, t) {
            e.textContent = t
        }, e.getTextContent = function(e) {
            return e.textContent
        }) : "innerText" in t ? (e.setTextContent = function(e, t) {
            e.innerText = t
        }, e.getTextContent = function(e) {
            return e.innerText
        }) : (e.setTextContent = function(e, t) {
            e.nodeValue = t
        }, e.getTextContent = function(e) {
            return e.nodeValue
        })
    }(wysihtml5.dom), wysihtml5.quirks.cleanPastedHTML = function() {
        function e(e, n, i) {
            n = n || t, i = i || e.ownerDocument || document;
            var a, r, o, s, d, l = "string" == typeof e,
                u = 0;
            a = l ? wysihtml5.dom.getAsDom(e, i) : e;
            for (d in n)
                for (o = a.querySelectorAll(d), r = n[d], s = o.length; s > u; u++) r(o[u]);
            return o = e = n = null, l ? a.innerHTML : a
        }
        var t = {
            "a u": wysihtml5.dom.replaceWithChildNodes
        };
        return e
    }(),
    function(e) {
        var t = e.dom;
        e.quirks.ensureProperClearing = function() {
            var e = function() {
                var e = this;
                setTimeout(function() {
                    var t = e.innerHTML.toLowerCase();
                    ("<p>&nbsp;</p>" == t || "<p>&nbsp;</p><p>&nbsp;</p>" == t) && (e.innerHTML = "")
                }, 0)
            };
            return function(n) {
                t.observe(n.element, ["cut", "keydown"], e)
            }
        }(), e.quirks.ensureProperClearingOfLists = function() {
            var n = ["OL", "UL", "MENU"],
                i = function(i, a) {
                    if (a.firstChild && e.lang.array(n).contains(a.firstChild.nodeName)) {
                        var r = t.getParentElement(i, {
                            nodeName: n
                        });
                        if (r) {
                            var o = r == a.firstChild;
                            if (o) {
                                var s = r.childNodes.length <= 1;
                                if (s) {
                                    var d = r.firstChild ? "" === r.firstChild.innerHTML : !0;
                                    d && r.parentNode.removeChild(r)
                                }
                            }
                        }
                    }
                };
            return function(n) {
                t.observe(n.element, "keydown", function(t) {
                    if (t.keyCode === e.BACKSPACE_KEY) {
                        var a = n.selection.getSelectedNode();
                        i(a, n.element)
                    }
                })
            }
        }()
    }(wysihtml5),
    function(e) {
        var t = "%7E";
        e.quirks.getCorrectInnerHTML = function(n) {
            var i = n.innerHTML;
            if (-1 === i.indexOf(t)) return i;
            var a, r, o, s, d = n.querySelectorAll("[href*='~'], [src*='~']");
            for (s = 0, o = d.length; o > s; s++) a = d[s].href || d[s].src, r = e.lang.string(a).replace("~").by(t), i = e.lang.string(i).replace(r).by(a);
            return i
        }
    }(wysihtml5),
    function(e) {
        var t = e.dom,
            n = ["LI", "P", "H1", "H2", "H3", "H4", "H5", "H6"],
            i = ["UL", "OL", "MENU"];
        e.quirks.insertLineBreakOnReturn = function(a) {
            function r(n) {
                var i = t.getParentElement(n, {
                    nodeName: ["P", "DIV"]
                }, 2);
                if (i) {
                    var r = document.createTextNode(e.INVISIBLE_SPACE);
                    t.insert(r).before(i), t.replaceWithChildNodes(i), a.selection.selectNode(r)
                }
            }

            function o(o) {
                var s = o.keyCode;
                if (!(o.shiftKey || s !== e.ENTER_KEY && s !== e.BACKSPACE_KEY)) {
                    var d = (o.target, a.selection.getSelectedNode()),
                        l = t.getParentElement(d, {
                            nodeName: n
                        }, 4);
                    return l ? ("LI" !== l.nodeName || s !== e.ENTER_KEY && s !== e.BACKSPACE_KEY ? l.nodeName.match(/H[1-6]/) && s === e.ENTER_KEY && setTimeout(function() {
                        r(a.selection.getSelectedNode())
                    }, 0) : setTimeout(function() {
                        var e, n = a.selection.getSelectedNode();
                        n && (e = t.getParentElement(n, {
                            nodeName: i
                        }, 2), e || r(n))
                    }, 0), void 0) : (s !== e.ENTER_KEY || e.browser.insertsLineBreaksOnReturn() || (a.commands.exec("insertLineBreak"), o.preventDefault()), void 0)
                }
            }
            t.observe(a.element.ownerDocument, "keydown", o)
        }
    }(wysihtml5),
    function(e) {
        var t = "wysihtml5-quirks-redraw";
        e.quirks.redraw = function(n) {
            e.dom.addClass(n, t), e.dom.removeClass(n, t);
            try {
                var i = n.ownerDocument;
                i.execCommand("italic", !1, null), i.execCommand("italic", !1, null)
            } catch (a) {}
        }
    }(wysihtml5),
    function(e) {
        function t(e) {
            var t = 0;
            if (e.parentNode)
                do t += e.offsetTop || 0, e = e.offsetParent; while (e);
            return t
        }
        var n = e.dom;
        e.Selection = Base.extend({
            constructor: function(e) {
                window.rangy.init(), this.editor = e, this.composer = e.composer, this.doc = this.composer.doc
            },
            getBookmark: function() {
                var e = this.getRange();
                return e && e.cloneRange()
            },
            setBookmark: function(e) {
                e && this.setSelection(e)
            },
            setBefore: function(e) {
                var t = rangy.createRange(this.doc);
                return t.setStartBefore(e), t.setEndBefore(e), this.setSelection(t)
            },
            setAfter: function(e) {
                var t = rangy.createRange(this.doc);
                return t.setStartAfter(e), t.setEndAfter(e), this.setSelection(t)
            },
            selectNode: function(t) {
                var i = rangy.createRange(this.doc),
                    a = t.nodeType === e.ELEMENT_NODE,
                    r = "canHaveHTML" in t ? t.canHaveHTML : "IMG" !== t.nodeName,
                    o = a ? t.innerHTML : t.data,
                    s = "" === o || o === e.INVISIBLE_SPACE,
                    d = n.getStyle("display").from(t),
                    l = "block" === d || "list-item" === d;
                if (s && a && r) try {
                    t.innerHTML = e.INVISIBLE_SPACE
                } catch (u) {}
                r ? i.selectNodeContents(t) : i.selectNode(t), r && s && a ? i.collapse(l) : r && s && (i.setStartAfter(t), i.setEndAfter(t)), this.setSelection(i)
            },
            getSelectedNode: function(e) {
                var t, n;
                return e && this.doc.selection && "Control" === this.doc.selection.type && (n = this.doc.selection.createRange(), n && n.length) ? n.item(0) : (t = this.getSelection(this.doc), t.focusNode === t.anchorNode ? t.focusNode : (n = this.getRange(this.doc), n ? n.commonAncestorContainer : this.doc.body))
            },
            executeAndRestore: function(t, n) {
                var i, a = this.doc.body,
                    r = n && a.scrollTop,
                    o = n && a.scrollLeft,
                    s = "_wysihtml5-temp-placeholder",
                    d = '<span class="' + s + '">' + e.INVISIBLE_SPACE + "</span>",
                    l = this.getRange(this.doc);
                if (!l) return t(a, a), void 0;
                var u = l.createContextualFragment(d);
                l.insertNode(u);
                try {
                    t(l.startContainer, l.endContainer)
                } catch (c) {
                    setTimeout(function() {
                        throw c
                    }, 0)
                }
                caretPlaceholder = this.doc.querySelector("." + s), caretPlaceholder ? (i = rangy.createRange(this.doc), i.selectNode(caretPlaceholder), i.deleteContents(), this.setSelection(i)) : a.focus(), n && (a.scrollTop = r, a.scrollLeft = o);
                try {
                    caretPlaceholder.parentNode.removeChild(caretPlaceholder)
                } catch (p) {}
            },
            executeAndRestoreSimple: function(e) {
                var t, n, i, a, r, o = this.getRange(),
                    s = this.doc.body;
                if (!o) return e(s, s), void 0;
                a = o.getNodes([3]), n = a[0] || o.startContainer, i = a[a.length - 1] || o.endContainer, r = {
                    collapsed: o.collapsed,
                    startContainer: n,
                    startOffset: n === o.startContainer ? o.startOffset : 0,
                    endContainer: i,
                    endOffset: i === o.endContainer ? o.endOffset : i.length
                };
                try {
                    e(o.startContainer, o.endContainer)
                } catch (d) {
                    setTimeout(function() {
                        throw d
                    }, 0)
                }
                t = rangy.createRange(this.doc);
                try {
                    t.setStart(r.startContainer, r.startOffset)
                } catch (l) {}
                try {
                    t.setEnd(r.endContainer, r.endOffset)
                } catch (u) {}
                try {
                    this.setSelection(t)
                } catch (c) {}
            },
            insertHTML: function(e) {
                var t = rangy.createRange(this.doc),
                    n = t.createContextualFragment(e),
                    i = n.lastChild;
                this.insertNode(n), i && this.setAfter(i)
            },
            insertNode: function(e) {
                var t = this.getRange();
                t && t.insertNode(e)
            },
            surround: function(e) {
                var t = this.getRange();
                if (t) try {
                    t.surroundContents(e), this.selectNode(e)
                } catch (n) {
                    e.appendChild(t.extractContents()), t.insertNode(e)
                }
            },
            scrollIntoView: function() {
                var n, i = this.doc,
                    a = i.documentElement.scrollHeight > i.documentElement.offsetHeight,
                    r = i._wysihtml5ScrollIntoViewElement = i._wysihtml5ScrollIntoViewElement || function() {
                        var t = i.createElement("span");
                        return t.innerHTML = e.INVISIBLE_SPACE, t
                    }();
                a && (this.insertNode(r), n = t(r), r.parentNode.removeChild(r), n > i.body.scrollTop && (i.body.scrollTop = n))
            },
            selectLine: function() {
                e.browser.supportsSelectionModify() ? this._selectLine_W3C() : this.doc.selection && this._selectLine_MSIE()
            },
            _selectLine_W3C: function() {
                var e = this.doc.defaultView,
                    t = e.getSelection();
                t.modify("extend", "left", "lineboundary"), t.modify("extend", "right", "lineboundary")
            },
            _selectLine_MSIE: function() {
                var e, t, n, i, a, r = this.doc.selection.createRange(),
                    o = r.boundingTop,
                    s = (r.boundingHeight, this.doc.body.scrollWidth);
                if (r.moveToPoint) {
                    for (0 === o && (n = this.doc.createElement("span"), this.insertNode(n), o = n.offsetTop, n.parentNode.removeChild(n)), o += 1, i = -10; s > i; i += 2) try {
                        r.moveToPoint(i, o);
                        break
                    } catch (d) {}
                    for (e = o, t = this.doc.selection.createRange(), a = s; a >= 0; a--) try {
                        t.moveToPoint(a, e);
                        break
                    } catch (l) {}
                    r.setEndPoint("EndToEnd", t), r.select()
                }
            },
            getText: function() {
                var e = this.getSelection();
                return e ? e.toString() : ""
            },
            getNodes: function(e, t) {
                var n = this.getRange();
                return n ? n.getNodes([e], t) : []
            },
            getRange: function() {
                var e = this.getSelection();
                return e && e.rangeCount && e.getRangeAt(0)
            },
            getSelection: function() {
                return rangy.getSelection(this.doc.defaultView || this.doc.parentWindow)
            },
            setSelection: function(e) {
                var t = this.doc.defaultView || this.doc.parentWindow,
                    n = rangy.getSelection(t);
                return n.setSingleRange(e)
            }
        })
    }(wysihtml5),
    function(e, t) {
        function n(e, t, n) {
            if (!e.className) return !1;
            var i = e.className.match(n) || [];
            return i[i.length - 1] === t
        }

        function i(e, t, n) {
            e.className ? (a(e, n), e.className += " " + t) : e.className = t
        }

        function a(e, t) {
            e.className && (e.className = e.className.replace(t, ""))
        }

        function r(e, t) {
            return e.className.replace(f, " ") == t.className.replace(f, " ")
        }

        function o(e) {
            for (var t = e.parentNode; e.firstChild;) t.insertBefore(e.firstChild, e);
            t.removeChild(e)
        }

        function s(e, t) {
            if (e.attributes.length != t.attributes.length) return !1;
            for (var n, i, a, r = 0, o = e.attributes.length; o > r; ++r)
                if (n = e.attributes[r], a = n.name, "class" != a) {
                    if (i = t.attributes.getNamedItem(a), n.specified != i.specified) return !1;
                    if (n.specified && n.nodeValue !== i.nodeValue) return !1
                }
            return !0
        }

        function d(e, n) {
            return t.dom.isCharacterDataNode(e) ? 0 == n ? !!e.previousSibling : n == e.length ? !!e.nextSibling : !0 : n > 0 && n < e.childNodes.length
        }

        function l(e, n, i) {
            var a;
            if (t.dom.isCharacterDataNode(n) && (0 == i ? (i = t.dom.getNodeIndex(n), n = n.parentNode) : i == n.length ? (i = t.dom.getNodeIndex(n) + 1, n = n.parentNode) : a = t.dom.splitDataNode(n, i)), !a) {
                a = n.cloneNode(!1), a.id && a.removeAttribute("id");
                for (var r; r = n.childNodes[i];) a.appendChild(r);
                t.dom.insertAfter(a, n)
            }
            return n == e ? a : l(e, a.parentNode, t.dom.getNodeIndex(a))
        }

        function u(t) {
            this.isElementMerge = t.nodeType == e.ELEMENT_NODE, this.firstTextNode = this.isElementMerge ? t.lastChild : t, this.textNodes = [this.firstTextNode]
        }

        function c(e, t, n, i) {
            this.tagNames = e || [p], this.cssClass = t || "", this.similarClassRegExp = n, this.normalize = i, this.applyToAnyTagName = !1
        }
        var p = "span",
            f = /\s+/g;
        u.prototype = {
            doMerge: function() {
                for (var e, t, n, i = [], a = 0, r = this.textNodes.length; r > a; ++a) e = this.textNodes[a], t = e.parentNode, i[a] = e.data, a && (t.removeChild(e), t.hasChildNodes() || t.parentNode.removeChild(t));
                return this.firstTextNode.data = n = i.join(""), n
            },
            getLength: function() {
                for (var e = this.textNodes.length, t = 0; e--;) t += this.textNodes[e].length;
                return t
            },
            toString: function() {
                for (var e = [], t = 0, n = this.textNodes.length; n > t; ++t) e[t] = "'" + this.textNodes[t].data + "'";
                return "[Merge(" + e.join(",") + ")]"
            }
        }, c.prototype = {
            getAncestorWithClass: function(i) {
                for (var a; i;) {
                    if (a = this.cssClass ? n(i, this.cssClass, this.similarClassRegExp) : !0, i.nodeType == e.ELEMENT_NODE && t.dom.arrayContains(this.tagNames, i.tagName.toLowerCase()) && a) return i;
                    i = i.parentNode
                }
                return !1
            },
            postApply: function(e, t) {
                for (var n, i, a, r = e[0], o = e[e.length - 1], s = [], d = r, l = o, c = 0, p = o.length, f = 0, h = e.length; h > f; ++f) i = e[f], a = this.getAdjacentMergeableTextNode(i.parentNode, !1), a ? (n || (n = new u(a), s.push(n)), n.textNodes.push(i), i === r && (d = n.firstTextNode, c = d.length), i === o && (l = n.firstTextNode, p = n.getLength())) : n = null;
                var m = this.getAdjacentMergeableTextNode(o.parentNode, !0);
                if (m && (n || (n = new u(o), s.push(n)), n.textNodes.push(m)), s.length) {
                    for (f = 0, h = s.length; h > f; ++f) s[f].doMerge();
                    t.setStart(d, c), t.setEnd(l, p)
                }
            },
            getAdjacentMergeableTextNode: function(t, n) {
                var i, a = t.nodeType == e.TEXT_NODE,
                    r = a ? t.parentNode : t,
                    o = n ? "nextSibling" : "previousSibling";
                if (a) {
                    if (i = t[o], i && i.nodeType == e.TEXT_NODE) return i
                } else if (i = r[o], i && this.areElementsMergeable(t, i)) return i[n ? "firstChild" : "lastChild"];
                return null
            },
            areElementsMergeable: function(e, n) {
                return t.dom.arrayContains(this.tagNames, (e.tagName || "").toLowerCase()) && t.dom.arrayContains(this.tagNames, (n.tagName || "").toLowerCase()) && r(e, n) && s(e, n)
            },
            createContainer: function(e) {
                var t = e.createElement(this.tagNames[0]);
                return this.cssClass && (t.className = this.cssClass), t
            },
            applyToTextNode: function(e) {
                var n = e.parentNode;
                if (1 == n.childNodes.length && t.dom.arrayContains(this.tagNames, n.tagName.toLowerCase())) this.cssClass && i(n, this.cssClass, this.similarClassRegExp);
                else {
                    var a = this.createContainer(t.dom.getDocument(e));
                    e.parentNode.insertBefore(a, e), a.appendChild(e)
                }
            },
            isRemovable: function(n) {
                return t.dom.arrayContains(this.tagNames, n.tagName.toLowerCase()) && e.lang.string(n.className).trim() == this.cssClass
            },
            undoToTextNode: function(e, t, n) {
                if (!t.containsNode(n)) {
                    var i = t.cloneRange();
                    i.selectNode(n), i.isPointInRange(t.endContainer, t.endOffset) && d(t.endContainer, t.endOffset) && (l(n, t.endContainer, t.endOffset), t.setEndAfter(n)), i.isPointInRange(t.startContainer, t.startOffset) && d(t.startContainer, t.startOffset) && (n = l(n, t.startContainer, t.startOffset))
                }
                this.similarClassRegExp && a(n, this.similarClassRegExp), this.isRemovable(n) && o(n)
            },
            applyToRange: function(t) {
                var n = t.getNodes([e.TEXT_NODE]);
                if (!n.length) try {
                    var i = this.createContainer(t.endContainer.ownerDocument);
                    return t.surroundContents(i), this.selectNode(t, i), void 0
                } catch (a) {}
                if (t.splitBoundaries(), n = t.getNodes([e.TEXT_NODE]), n.length) {
                    for (var r, o = 0, s = n.length; s > o; ++o) r = n[o], this.getAncestorWithClass(r) || this.applyToTextNode(r);
                    t.setStart(n[0], 0), r = n[n.length - 1], t.setEnd(r, r.length), this.normalize && this.postApply(n, t)
                }
            },
            undoToRange: function(t) {
                var n, i, a = t.getNodes([e.TEXT_NODE]);
                if (a.length) t.splitBoundaries(), a = t.getNodes([e.TEXT_NODE]);
                else {
                    var r = t.endContainer.ownerDocument,
                        o = r.createTextNode(e.INVISIBLE_SPACE);
                    t.insertNode(o), t.selectNode(o), a = [o]
                }
                for (var s = 0, d = a.length; d > s; ++s) n = a[s], i = this.getAncestorWithClass(n), i && this.undoToTextNode(n, t, i);
                1 == d ? this.selectNode(t, a[0]) : (t.setStart(a[0], 0), n = a[a.length - 1], t.setEnd(n, n.length), this.normalize && this.postApply(a, t))
            },
            selectNode: function(t, n) {
                var i = n.nodeType === e.ELEMENT_NODE,
                    a = "canHaveHTML" in n ? n.canHaveHTML : !0,
                    r = i ? n.innerHTML : n.data,
                    o = "" === r || r === e.INVISIBLE_SPACE;
                if (o && i && a) try {
                    n.innerHTML = e.INVISIBLE_SPACE
                } catch (s) {}
                t.selectNodeContents(n), o && i ? t.collapse(!1) : o && (t.setStartAfter(n), t.setEndAfter(n))
            },
            getTextSelectedByRange: function(e, t) {
                var n = t.cloneRange();
                n.selectNodeContents(e);
                var i = n.intersection(t),
                    a = i ? i.toString() : "";
                return n.detach(), a
            },
            isAppliedToRange: function(t) {
                var n, i = [],
                    a = t.getNodes([e.TEXT_NODE]);
                if (!a.length) return n = this.getAncestorWithClass(t.startContainer), n ? [n] : !1;
                for (var r, o = 0, s = a.length; s > o; ++o) {
                    if (r = this.getTextSelectedByRange(a[o], t), n = this.getAncestorWithClass(a[o]), "" != r && !n) return !1;
                    i.push(n)
                }
                return i
            },
            toggleRange: function(e) {
                this.isAppliedToRange(e) ? this.undoToRange(e) : this.applyToRange(e)
            }
        }, e.selection.HTMLApplier = c
    }(wysihtml5, rangy), wysihtml5.Commands = Base.extend({
        constructor: function(e) {
            this.editor = e, this.composer = e.composer, this.doc = this.composer.doc
        },
        support: function(e) {
            return wysihtml5.browser.supportsCommand(this.doc, e)
        },
        exec: function(e, t) {
            var n = wysihtml5.commands[e],
                i = wysihtml5.lang.array(arguments).get(),
                a = n && n.exec,
                r = null;
            if (this.editor.fire("beforecommand:composer"), a) i.unshift(this.composer), r = a.apply(n, i);
            else try {
                r = this.doc.execCommand(e, !1, t)
            } catch (o) {}
            return this.editor.fire("aftercommand:composer"), r
        },
        state: function(e) {
            var t = wysihtml5.commands[e],
                n = wysihtml5.lang.array(arguments).get(),
                i = t && t.state;
            if (i) return n.unshift(this.composer), i.apply(t, n);
            try {
                return this.doc.queryCommandState(e)
            } catch (a) {
                return !1
            }
        },
        value: function(e) {
            var t = wysihtml5.commands[e],
                n = t && t.value;
            if (n) return n.call(t, this.composer, e);
            try {
                return this.doc.queryCommandValue(e)
            } catch (i) {
                return null
            }
        }
    }),
    function(e) {
        var t;
        e.commands.bold = {
            exec: function(t, n) {
                return e.commands.formatInline.exec(t, n, "b")
            },
            state: function(t, n) {
                return e.commands.formatInline.state(t, n, "b")
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        function t(e, t) {
            for (var n, i, a, o = t.length, s = 0; o > s; s++) n = t[s], i = r.getParentElement(n, {
                nodeName: "code"
            }), a = r.getTextContent(n), a.match(r.autoLink.URL_REG_EXP) && !i ? i = r.renameElement(n, "code") : r.replaceWithChildNodes(n)
        }

        function n(t, n) {
            var o, s, d, l, u, c, p, f, h, m = t.doc,
                g = "_wysihtml5-temp-" + +new Date,
                v = /non-matching-class/g,
                y = 0;
            for (e.commands.formatInline.exec(t, i, a, g, v), s = m.querySelectorAll(a + "." + g), o = s.length; o > y; y++) {
                d = s[y], d.removeAttribute("class");
                for (h in n) d.setAttribute(h, n[h])
            }
            c = d, 1 === o && (p = r.getTextContent(d), l = !!d.querySelector("*"), u = "" === p || p === e.INVISIBLE_SPACE, !l && u && (r.setTextContent(d, n.text || d.href), f = m.createTextNode(" "), t.selection.setAfter(d), t.selection.insertNode(f), c = f)), t.selection.setAfter(c)
        }
        var i, a = "A",
            r = e.dom;
        e.commands.createLink = {
            exec: function(e, i, a) {
                var r = this.state(e, i);
                r ? e.selection.executeAndRestore(function() {
                    t(e, r)
                }) : (a = "object" == typeof a ? a : {
                    href: a
                }, n(e, a))
            },
            state: function(t, n) {
                return e.commands.formatInline.state(t, n, "A")
            },
            value: function() {
                return i
            }
        }
    }(wysihtml5),
    function(e) {
        var t, n = /wysiwyg-font-size-[a-z\-]+/g;
        e.commands.fontSize = {
            exec: function(t, i, a) {
                return e.commands.formatInline.exec(t, i, "span", "wysiwyg-font-size-" + a, n)
            },
            state: function(t, i, a) {
                return e.commands.formatInline.state(t, i, "span", "wysiwyg-font-size-" + a, n)
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t, n = /wysiwyg-color-[a-z]+/g;
        e.commands.foreColor = {
            exec: function(t, i, a) {
                return e.commands.formatInline.exec(t, i, "span", "wysiwyg-color-" + a, n)
            },
            state: function(t, i, a) {
                return e.commands.formatInline.state(t, i, "span", "wysiwyg-color-" + a, n)
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        function t(e, t, i) {
            e.className ? (n(e, i), e.className += " " + t) : e.className = t
        }

        function n(e, t) {
            e.className = e.className.replace(t, "")
        }

        function i(t) {
            return t.nodeType === e.TEXT_NODE && !e.lang.string(t.data).trim()
        }

        function a(e) {
            for (var t = e.previousSibling; t && i(t);) t = t.previousSibling;
            return t
        }

        function r(e) {
            for (var t = e.nextSibling; t && i(t);) t = t.nextSibling;
            return t
        }

        function o(e) {
            var t = e.ownerDocument,
                n = r(e),
                i = a(e);
            n && !u(n) && e.parentNode.insertBefore(t.createElement("br"), n), i && !u(i) && e.parentNode.insertBefore(t.createElement("br"), e)
        }

        function s(e) {
            var t = r(e),
                n = a(e);
            t && l(t) && t.parentNode.removeChild(t), n && l(n) && n.parentNode.removeChild(n)
        }

        function d(e) {
            var t = e.lastChild;
            t && l(t) && t.parentNode.removeChild(t)
        }

        function l(e) {
            return "BR" === e.nodeName
        }

        function u(e) {
            return l(e) ? !0 : "block" === m.getStyle("display").from(e) ? !0 : !1
        }

        function c(t, n, i, a) {
            if (a) var r = m.observe(t, "DOMNodeInserted", function(t) {
                var n, i = t.target;
                i.nodeType === e.ELEMENT_NODE && (n = m.getStyle("display").from(i), "inline" !== n.substr(0, 6) && (i.className += " " + a))
            });
            t.execCommand(n, !1, i), r && r.stop()
        }

        function p(e, t) {
            e.selection.selectLine(), e.selection.surround(t), s(t), d(t), e.selection.selectNode(t)
        }

        function f(t) {
            return !!e.lang.string(t.className).trim()
        }
        var h, m = e.dom,
            g = "DIV",
            v = ["H1", "H2", "H3", "H4", "H5", "H6", "P", "BLOCKQUOTE", g];
        e.commands.formatBlock = {
            exec: function(i, a, r, s, d) {
                var l, u = i.doc,
                    h = this.state(i, a, r, s, d);
                return r = "string" == typeof r ? r.toUpperCase() : r, h ? (i.selection.executeAndRestoreSimple(function() {
                    d && n(h, d);
                    var e = f(h);
                    e || h.nodeName !== (r || g) ? e && m.renameElement(h, g) : (o(h), m.replaceWithChildNodes(h))
                }), void 0) : (null === r || e.lang.array(v).contains(r)) && (l = i.selection.getSelectedNode(), h = m.getParentElement(l, {
                    nodeName: v
                })) ? (i.selection.executeAndRestoreSimple(function() {
                    r && (h = m.renameElement(h, r)), s && t(h, s, d)
                }), void 0) : i.commands.support(a) ? (c(u, a, r || g, s), void 0) : (h = u.createElement(r || g), s && (h.className = s), p(i, h), void 0)
            },
            state: function(e, t, n, i, a) {
                n = "string" == typeof n ? n.toUpperCase() : n;
                var r = e.selection.getSelectedNode();
                return m.getParentElement(r, {
                    nodeName: n,
                    className: i,
                    classRegExp: a
                })
            },
            value: function() {
                return h
            }
        }
    }(wysihtml5),
    function(e) {
        function t(e) {
            var t = a[e];
            return t ? [e.toLowerCase(), t.toLowerCase()] : [e.toLowerCase()]
        }

        function n(n, i, a) {
            var o = n + ":" + i;
            return r[o] || (r[o] = new e.selection.HTMLApplier(t(n), i, a, !0)), r[o]
        }
        var i, a = {
                strong: "b",
                em: "i",
                b: "strong",
                i: "em"
            },
            r = {};
        e.commands.formatInline = {
            exec: function(e, t, i, a, r) {
                var o = e.selection.getRange();
                return o ? (n(i, a, r).toggleRange(o), e.selection.setSelection(o), void 0) : !1
            },
            state: function(t, i, r, o, s) {
                var d, l = t.doc,
                    u = a[r] || r;
                return e.dom.hasElementWithTagName(l, r) || e.dom.hasElementWithTagName(l, u) ? o && !e.dom.hasElementWithClassName(l, o) ? !1 : (d = t.selection.getRange(), d ? n(r, o, s).isAppliedToRange(d) : !1) : !1
            },
            value: function() {
                return i
            }
        }
    }(wysihtml5),
    function(e) {
        var t;
        e.commands.insertHTML = {
            exec: function(e, t, n) {
                e.commands.support(t) ? e.doc.execCommand(t, !1, n) : e.selection.insertHTML(n)
            },
            state: function() {
                return !1
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t = "IMG";
        e.commands.insertImage = {
            exec: function(n, i, a) {
                a = "object" == typeof a ? a : {
                    src: a
                };
                var r, o, s, d = n.doc,
                    l = this.state(n);
                if (l) return n.selection.setBefore(l), s = l.parentNode, s.removeChild(l), e.dom.removeEmptyTextNodes(s), "A" !== s.nodeName || s.firstChild || (n.selection.setAfter(s), s.parentNode.removeChild(s)), e.quirks.redraw(n.element), void 0;
                l = d.createElement(t);
                for (o in a) l[o] = a[o];
                n.selection.insertNode(l), e.browser.hasProblemsSettingCaretAfterImg() ? (r = d.createTextNode(e.INVISIBLE_SPACE), n.selection.insertNode(r), n.selection.setAfter(r)) : n.selection.setAfter(l)
            },
            state: function(n) {
                var i, a, r, o = n.doc;
                return e.dom.hasElementWithTagName(o, t) ? (i = n.selection.getSelectedNode()) ? i.nodeName === t ? i : i.nodeType !== e.ELEMENT_NODE ? !1 : (a = n.selection.getText(), (a = e.lang.string(a).trim()) ? !1 : (r = n.selection.getNodes(e.ELEMENT_NODE, function(e) {
                    return "IMG" === e.nodeName
                }), 1 !== r.length ? !1 : r[0])) : !1 : !1
            },
            value: function(e) {
                var t = this.state(e);
                return t && t.src
            }
        }
    }(wysihtml5),
    function(e) {
        var t, n = "<br>" + (e.browser.needsSpaceAfterLineBreak() ? " " : "");
        e.commands.insertLineBreak = {
            exec: function(t, i) {
                t.commands.support(i) ? (t.doc.execCommand(i, !1, null), e.browser.autoScrollsToCaret() || t.selection.scrollIntoView()) : t.commands.exec("insertHTML", n)
            },
            state: function() {
                return !1
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t;
        e.commands.insertOrderedList = {
            exec: function(t, n) {
                var i, a, r = t.doc,
                    o = t.selection.getSelectedNode(),
                    s = e.dom.getParentElement(o, {
                        nodeName: "OL"
                    }),
                    d = e.dom.getParentElement(o, {
                        nodeName: "UL"
                    }),
                    l = "_wysihtml5-temp-" + (new Date).getTime();
                return t.commands.support(n) ? (r.execCommand(n, !1, null), void 0) : (s ? t.selection.executeAndRestoreSimple(function() {
                    e.dom.resolveList(s)
                }) : d ? t.selection.executeAndRestoreSimple(function() {
                    e.dom.renameElement(d, "ol")
                }) : (t.commands.exec("formatBlock", "div", l), a = r.querySelector("." + l), i = "" === a.innerHTML || a.innerHTML === e.INVISIBLE_SPACE, t.selection.executeAndRestoreSimple(function() {
                    s = e.dom.convertToList(a, "ol")
                }), i && t.selection.selectNode(s.querySelector("li"))), void 0)
            },
            state: function(t) {
                var n = t.selection.getSelectedNode();
                return e.dom.getParentElement(n, {
                    nodeName: "OL"
                })
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t;
        e.commands.insertUnorderedList = {
            exec: function(t, n) {
                var i, a, r = t.doc,
                    o = t.selection.getSelectedNode(),
                    s = e.dom.getParentElement(o, {
                        nodeName: "UL"
                    }),
                    d = e.dom.getParentElement(o, {
                        nodeName: "OL"
                    }),
                    l = "_wysihtml5-temp-" + (new Date).getTime();
                return t.commands.support(n) ? (r.execCommand(n, !1, null), void 0) : (s ? t.selection.executeAndRestoreSimple(function() {
                    e.dom.resolveList(s)
                }) : d ? t.selection.executeAndRestoreSimple(function() {
                    e.dom.renameElement(d, "ul")
                }) : (t.commands.exec("formatBlock", "div", l), a = r.querySelector("." + l), i = "" === a.innerHTML || a.innerHTML === e.INVISIBLE_SPACE, t.selection.executeAndRestoreSimple(function() {
                    s = e.dom.convertToList(a, "ul")
                }), i && t.selection.selectNode(s.querySelector("li"))), void 0)
            },
            state: function(t) {
                var n = t.selection.getSelectedNode();
                return e.dom.getParentElement(n, {
                    nodeName: "UL"
                })
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t;
        e.commands.italic = {
            exec: function(t, n) {
                return e.commands.formatInline.exec(t, n, "i")
            },
            state: function(t, n) {
                return e.commands.formatInline.state(t, n, "i")
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t, n = "wysiwyg-text-align-center",
            i = /wysiwyg-text-align-[a-z]+/g;
        e.commands.justifyCenter = {
            exec: function(t) {
                return e.commands.formatBlock.exec(t, "formatBlock", null, n, i)
            },
            state: function(t) {
                return e.commands.formatBlock.state(t, "formatBlock", null, n, i)
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t, n = "wysiwyg-text-align-left",
            i = /wysiwyg-text-align-[a-z]+/g;
        e.commands.justifyLeft = {
            exec: function(t) {
                return e.commands.formatBlock.exec(t, "formatBlock", null, n, i)
            },
            state: function(t) {
                return e.commands.formatBlock.state(t, "formatBlock", null, n, i)
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t, n = "wysiwyg-text-align-right",
            i = /wysiwyg-text-align-[a-z]+/g;
        e.commands.justifyRight = {
            exec: function(t) {
                return e.commands.formatBlock.exec(t, "formatBlock", null, n, i)
            },
            state: function(t) {
                return e.commands.formatBlock.state(t, "formatBlock", null, n, i)
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        var t;
        e.commands.underline = {
            exec: function(t, n) {
                return e.commands.formatInline.exec(t, n, "u")
            },
            state: function(t, n) {
                return e.commands.formatInline.state(t, n, "u")
            },
            value: function() {
                return t
            }
        }
    }(wysihtml5),
    function(e) {
        function t(e) {
            for (var t; t = e.querySelector("._wysihtml5-temp");) t.parentNode.removeChild(t)
        }
        var n = 90,
            i = 89,
            a = 8,
            r = 46,
            o = 40,
            s = '<span id="_wysihtml5-undo" class="_wysihtml5-temp">' + e.INVISIBLE_SPACE + "</span>",
            d = '<span id="_wysihtml5-redo" class="_wysihtml5-temp">' + e.INVISIBLE_SPACE + "</span>",
            l = e.dom;
        e.UndoManager = e.lang.Dispatcher.extend({
            constructor: function(e) {
                this.editor = e, this.composer = e.composer, this.element = this.composer.element, this.history = [this.composer.getValue()], this.position = 1, this.composer.commands.support("insertHTML") && this._observe()
            },
            _observe: function() {
                var o, u = this,
                    c = this.composer.sandbox.getDocument();
                if (l.observe(this.element, "keydown", function(e) {
                        if (!e.altKey && (e.ctrlKey || e.metaKey)) {
                            var t = e.keyCode,
                                a = t === n && !e.shiftKey,
                                r = t === n && e.shiftKey || t === i;
                            a ? (u.undo(), e.preventDefault()) : r && (u.redo(), e.preventDefault())
                        }
                    }), l.observe(this.element, "keydown", function(e) {
                        var t = e.keyCode;
                        t !== o && (o = t, (t === a || t === r) && u.transact())
                    }), e.browser.hasUndoInContextMenu()) {
                    var p, f, h = function() {
                        t(c), clearInterval(p)
                    };
                    l.observe(this.element, "contextmenu", function() {
                        h(), u.composer.selection.executeAndRestoreSimple(function() {
                            u.element.lastChild && u.composer.selection.setAfter(u.element.lastChild), c.execCommand("insertHTML", !1, s), c.execCommand("insertHTML", !1, d), c.execCommand("undo", !1, null)
                        }), p = setInterval(function() {
                            c.getElementById("_wysihtml5-redo") ? (h(), u.redo()) : c.getElementById("_wysihtml5-undo") || (h(), u.undo())
                        }, 400), f || (f = !0, l.observe(document, "mousedown", h), l.observe(c, ["mousedown", "paste", "cut", "copy"], h))
                    })
                }
                this.editor.observe("newword:composer", function() {
                    u.transact()
                }).observe("beforecommand:composer", function() {
                    u.transact()
                })
            },
            transact: function() {
                var e = this.history[this.position - 1],
                    t = this.composer.getValue();
                if (t != e) {
                    var n = this.history.length = this.position;
                    n > o && (this.history.shift(), this.position--), this.position++, this.history.push(t)
                }
            },
            undo: function() {
                this.transact(), this.position <= 1 || (this.set(this.history[--this.position - 1]), this.editor.fire("undo:composer"))
            },
            redo: function() {
                this.position >= this.history.length || (this.set(this.history[++this.position - 1]), this.editor.fire("redo:composer"))
            },
            set: function(e) {
                this.composer.setValue(e), this.editor.focus(!0)
            }
        })
    }(wysihtml5), wysihtml5.views.View = Base.extend({
        constructor: function(e, t, n) {
            this.parent = e, this.element = t, this.config = n, this._observeViewChange()
        },
        _observeViewChange: function() {
            var e = this;
            this.parent.observe("beforeload", function() {
                e.parent.observe("change_view", function(t) {
                    t === e.name ? (e.parent.currentView = e, e.show(), setTimeout(function() {
                        e.focus()
                    }, 0)) : e.hide()
                })
            })
        },
        focus: function() {
            if (this.element.ownerDocument.querySelector(":focus") !== this.element) try {
                this.element.focus()
            } catch (e) {}
        },
        hide: function() {
            this.element.style.display = "none"
        },
        show: function() {
            this.element.style.display = ""
        },
        disable: function() {
            this.element.setAttribute("disabled", "disabled")
        },
        enable: function() {
            this.element.removeAttribute("disabled")
        }
    }),
    function(e) {
        var t = e.dom,
            n = e.browser;
        e.views.Composer = e.views.View.extend({
            name: "composer",
            CARET_HACK: "<br>",
            constructor: function(e, t, n) {
                this.base(e, t, n), this.textarea = this.parent.textarea, this._initSandbox()
            },
            clear: function() {
                this.element.innerHTML = n.displaysCaretInEmptyContentEditableCorrectly() ? "" : this.CARET_HACK
            },
            getValue: function(t) {
                var n = this.isEmpty() ? "" : e.quirks.getCorrectInnerHTML(this.element);
                return t && (n = this.parent.parse(n)), n = e.lang.string(n).replace(e.INVISIBLE_SPACE).by("")
            },
            setValue: function(e, t) {
                t && (e = this.parent.parse(e)), this.element.innerHTML = e
            },
            show: function() {
                this.iframe.style.display = this._displayStyle || "", this.disable(), this.enable()
            },
            hide: function() {
                this._displayStyle = t.getStyle("display").from(this.iframe), "none" === this._displayStyle && (this._displayStyle = null), this.iframe.style.display = "none"
            },
            disable: function() {
                this.element.removeAttribute("contentEditable"), this.base()
            },
            enable: function() {
                this.element.setAttribute("contentEditable", "true"), this.base()
            },
            focus: function(t) {
                e.browser.doesAsyncFocus() && this.hasPlaceholderSet() && this.clear(), this.base();
                var n = this.element.lastChild;
                t && n && ("BR" === n.nodeName ? this.selection.setBefore(this.element.lastChild) : this.selection.setAfter(this.element.lastChild))
            },
            getTextContent: function() {
                return t.getTextContent(this.element)
            },
            hasPlaceholderSet: function() {
                return this.getTextContent() == this.textarea.element.getAttribute("placeholder")
            },
            isEmpty: function() {
                var e = this.element.innerHTML,
                    t = "blockquote, ul, ol, img, embed, object, table, iframe, svg, video, audio, button, input, select, textarea";
                return "" === e || e === this.CARET_HACK || this.hasPlaceholderSet() || "" === this.getTextContent() && !this.element.querySelector(t)
            },
            _initSandbox: function() {
                var e = this;
                this.sandbox = new t.Sandbox(function() {
                    e._create()
                }, {
                    stylesheets: this.config.stylesheets
                }), this.iframe = this.sandbox.getIframe();
                var n = document.createElement("input");
                n.type = "hidden", n.name = "_wysihtml5_mode", n.value = 1;
                var i = this.textarea.element;
                t.insert(this.iframe).after(i), t.insert(n).after(i)
            },
            _create: function() {
                var i = this;
                this.doc = this.sandbox.getDocument(), this.element = this.doc.body, this.textarea = this.parent.textarea, this.element.innerHTML = this.textarea.getValue(!0), this.enable(), this.selection = new e.Selection(this.parent), this.commands = new e.Commands(this.parent), t.copyAttributes(["className", "spellcheck", "title", "lang", "dir", "accessKey"]).from(this.textarea.element).to(this.element), t.addClass(this.element, this.config.composerClassName), this.config.style && this.style(), this.observe();
                var a = this.config.name;
                a && (t.addClass(this.element, a), t.addClass(this.iframe, a));
                var r = "string" == typeof this.config.placeholder ? this.config.placeholder : this.textarea.element.getAttribute("placeholder");
                r && t.simulatePlaceholder(this.parent, this, r), this.commands.exec("styleWithCSS", !1), this._initAutoLinking(), this._initObjectResizing(), this._initUndoManager(), (this.textarea.element.hasAttribute("autofocus") || document.querySelector(":focus") == this.textarea.element) && setTimeout(function() {
                    i.focus()
                }, 100), e.quirks.insertLineBreakOnReturn(this), n.clearsContentEditableCorrectly() || e.quirks.ensureProperClearing(this), n.clearsListsInContentEditableCorrectly() || e.quirks.ensureProperClearingOfLists(this), this.initSync && this.config.sync && this.initSync(), this.textarea.hide(), this.parent.fire("beforeload").fire("load")
            },
            _initAutoLinking: function() {
                var i = this,
                    a = n.canDisableAutoLinking(),
                    r = n.doesAutoLinkingInContentEditable();
                if (a && this.commands.exec("autoUrlDetect", !1), this.config.autoLink) {
                    (!r || r && a) && this.parent.observe("newword:composer", function() {
                        i.selection.executeAndRestore(function(e, n) {
                            t.autoLink(n.parentNode)
                        })
                    });
                    var o = this.sandbox.getDocument().getElementsByTagName("a"),
                        s = t.autoLink.URL_REG_EXP,
                        d = function(n) {
                            var i = e.lang.string(t.getTextContent(n)).trim();
                            return "www." === i.substr(0, 4) && (i = "http://" + i), i
                        };
                    t.observe(this.element, "keydown", function(e) {
                        if (o.length) {
                            var n, a = i.selection.getSelectedNode(e.target.ownerDocument),
                                r = t.getParentElement(a, {
                                    nodeName: "A"
                                }, 4);
                            r && (n = d(r), setTimeout(function() {
                                var e = d(r);
                                e !== n && e.match(s) && r.setAttribute("href", e)
                            }, 0))
                        }
                    })
                }
            },
            _initObjectResizing: function() {
                var i = ["width", "height"],
                    a = i.length,
                    r = this.element;
                this.commands.exec("enableObjectResizing", this.config.allowObjectResizing), this.config.allowObjectResizing ? n.supportsEvent("resizeend") && t.observe(r, "resizeend", function(t) {
                    for (var n, o = t.target || t.srcElement, s = o.style, d = 0; a > d; d++) n = i[d], s[n] && (o.setAttribute(n, parseInt(s[n], 10)), s[n] = "");
                    e.quirks.redraw(r)
                }) : n.supportsEvent("resizestart") && t.observe(r, "resizestart", function(e) {
                    e.preventDefault()
                })
            },
            _initUndoManager: function() {
                new e.UndoManager(this.parent)
            }
        })
    }(wysihtml5),
    function(e) {
        var t = e.dom,
            n = document,
            i = window,
            a = n.createElement("div"),
            r = ["background-color", "color", "cursor", "font-family", "font-size", "font-style", "font-variant", "font-weight", "line-height", "letter-spacing", "text-align", "text-decoration", "text-indent", "text-rendering", "word-break", "word-wrap", "word-spacing"],
            o = ["background-color", "border-collapse", "border-bottom-color", "border-bottom-style", "border-bottom-width", "border-left-color", "border-left-style", "border-left-width", "border-right-color", "border-right-style", "border-right-width", "border-top-color", "border-top-style", "border-top-width", "clear", "display", "float", "margin-bottom", "margin-left", "margin-right", "margin-top", "outline-color", "outline-offset", "outline-width", "outline-style", "padding-left", "padding-right", "padding-top", "padding-bottom", "position", "top", "left", "right", "bottom", "z-index", "vertical-align", "text-align", "-webkit-box-sizing", "-moz-box-sizing", "-ms-box-sizing", "box-sizing", "-webkit-box-shadow", "-moz-box-shadow", "-ms-box-shadow", "box-shadow", "-webkit-border-top-right-radius", "-moz-border-radius-topright", "border-top-right-radius", "-webkit-border-bottom-right-radius", "-moz-border-radius-bottomright", "border-bottom-right-radius", "-webkit-border-bottom-left-radius", "-moz-border-radius-bottomleft", "border-bottom-left-radius", "-webkit-border-top-left-radius", "-moz-border-radius-topleft", "border-top-left-radius", "width", "height"],
            s = ["width", "height", "top", "left", "right", "bottom"],
            d = ["html             { height: 100%; }", "body             { min-height: 100%; padding: 0; margin: 0; margin-top: -1px; padding-top: 1px; }", "._wysihtml5-temp { display: none; }", e.browser.isGecko ? "body.placeholder { color: graytext !important; }" : "body.placeholder { color: #a9a9a9 !important; }", "body[disabled]   { background-color: #eee !important; color: #999 !important; cursor: default !important; }", "img:-moz-broken  { -moz-force-broken-image-icon: 1; height: 24px; width: 24px; }"],
            l = function(e) {
                if (e.setActive) try {
                    e.setActive()
                } catch (a) {} else {
                    var r = e.style,
                        o = n.documentElement.scrollTop || n.body.scrollTop,
                        s = n.documentElement.scrollLeft || n.body.scrollLeft,
                        d = {
                            position: r.position,
                            top: r.top,
                            left: r.left,
                            WebkitUserSelect: r.WebkitUserSelect
                        };
                    t.setStyles({
                        position: "absolute",
                        top: "-99999px",
                        left: "-99999px",
                        WebkitUserSelect: "none"
                    }).on(e), e.focus(), t.setStyles(d).on(e), i.scrollTo && i.scrollTo(s, o)
                }
            };
        e.views.Composer.prototype.style = function() {
            var u = this,
                c = n.querySelector(":focus"),
                p = this.textarea.element,
                f = p.hasAttribute("placeholder"),
                h = f && p.getAttribute("placeholder");
            this.focusStylesHost = this.focusStylesHost || a.cloneNode(!1), this.blurStylesHost = this.blurStylesHost || a.cloneNode(!1), f && p.removeAttribute("placeholder"), p === c && p.blur(), t.copyStyles(o).from(p).to(this.iframe).andTo(this.blurStylesHost), t.copyStyles(r).from(p).to(this.element).andTo(this.blurStylesHost), t.insertCSS(d).into(this.element.ownerDocument), l(p), t.copyStyles(o).from(p).to(this.focusStylesHost), t.copyStyles(r).from(p).to(this.focusStylesHost);
            var m = e.lang.array(o).without(["display"]);
            if (c ? c.focus() : p.blur(), f && p.setAttribute("placeholder", h), !e.browser.hasCurrentStyleProperty()) var g = t.observe(i, "resize", function() {
                if (!t.contains(document.documentElement, u.iframe)) return g.stop(), void 0;
                var e = t.getStyle("display").from(p),
                    n = t.getStyle("display").from(u.iframe);
                p.style.display = "", u.iframe.style.display = "none", t.copyStyles(s).from(p).to(u.iframe).andTo(u.focusStylesHost).andTo(u.blurStylesHost), u.iframe.style.display = n, p.style.display = e
            });
            return this.parent.observe("focus:composer", function() {
                t.copyStyles(m).from(u.focusStylesHost).to(u.iframe), t.copyStyles(r).from(u.focusStylesHost).to(u.element)
            }), this.parent.observe("blur:composer", function() {
                t.copyStyles(m).from(u.blurStylesHost).to(u.iframe), t.copyStyles(r).from(u.blurStylesHost).to(u.element)
            }), this
        }
    }(wysihtml5),
    function(e) {
        var t = e.dom,
            n = e.browser,
            i = {
                66: "bold",
                73: "italic",
                85: "underline"
            };
        e.views.Composer.prototype.observe = function() {
            var a = this,
                r = this.getValue(),
                o = this.sandbox.getIframe(),
                s = this.element,
                d = n.supportsEventsInIframeCorrectly() ? s : this.sandbox.getWindow(),
                l = n.supportsEvent("drop") ? ["drop", "paste"] : ["dragdrop", "paste"];
            t.observe(o, "DOMNodeRemoved", function() {
                clearInterval(u), a.parent.fire("destroy:composer")
            });
            var u = setInterval(function() {
                t.contains(document.documentElement, o) || (clearInterval(u), a.parent.fire("destroy:composer"))
            }, 250);
            t.observe(d, "focus", function() {
                a.parent.fire("focus").fire("focus:composer"), setTimeout(function() {
                    r = a.getValue()
                }, 0)
            }), t.observe(d, "blur", function() {
                r !== a.getValue() && a.parent.fire("change").fire("change:composer"), a.parent.fire("blur").fire("blur:composer")
            }), e.browser.isIos() && t.observe(s, "blur", function() {
                var e = s.ownerDocument.createElement("input"),
                    t = document.documentElement.scrollTop || document.body.scrollTop,
                    n = document.documentElement.scrollLeft || document.body.scrollLeft;
                try {
                    a.selection.insertNode(e)
                } catch (i) {
                    s.appendChild(e)
                }
                e.focus(), e.parentNode.removeChild(e), window.scrollTo(n, t)
            }), t.observe(s, "dragenter", function() {
                a.parent.fire("unset_placeholder")
            }), n.firesOnDropOnlyWhenOnDragOverIsCancelled() && t.observe(s, ["dragover", "dragenter"], function(e) {
                e.preventDefault()
            }), t.observe(s, l, function(e) {
                var t, i = e.dataTransfer;
                i && n.supportsDataTransfer() && (t = i.getData("text/html") || i.getData("text/plain")), t ? (s.focus(), a.commands.exec("insertHTML", t), a.parent.fire("paste").fire("paste:composer"), e.stopPropagation(), e.preventDefault()) : setTimeout(function() {
                    a.parent.fire("paste").fire("paste:composer")
                }, 0)
            }), t.observe(s, "keyup", function(t) {
                var n = t.keyCode;
                (n === e.SPACE_KEY || n === e.ENTER_KEY) && a.parent.fire("newword:composer")
            }), this.parent.observe("paste:composer", function() {
                setTimeout(function() {
                    a.parent.fire("newword:composer")
                }, 0)
            }), n.canSelectImagesInContentEditable() || t.observe(s, "mousedown", function(e) {
                var t = e.target;
                "IMG" === t.nodeName && (a.selection.selectNode(t), e.preventDefault())
            }), t.observe(s, "keydown", function(e) {
                var t = e.keyCode,
                    n = i[t];
                (e.ctrlKey || e.metaKey) && !e.altKey && n && (a.commands.exec(n), e.preventDefault())
            }), t.observe(s, "keydown", function(t) {
                var n, i = a.selection.getSelectedNode(!0),
                    r = t.keyCode;
                !i || "IMG" !== i.nodeName || r !== e.BACKSPACE_KEY && r !== e.DELETE_KEY || (n = i.parentNode, n.removeChild(i), "A" !== n.nodeName || n.firstChild || n.parentNode.removeChild(n), setTimeout(function() {
                    e.quirks.redraw(s)
                }, 0), t.preventDefault())
            });
            var c = {
                IMG: "Image: ",
                A: "Link: "
            };
            t.observe(s, "mouseover", function(e) {
                var t, n = e.target,
                    i = n.nodeName;
                if ("A" === i || "IMG" === i) {
                    var a = n.hasAttribute("title");
                    a || (t = c[i] + (n.getAttribute("href") || n.getAttribute("src")), n.setAttribute("title", t))
                }
            })
        }
    }(wysihtml5),
    function(e) {
        var t = 400;
        e.views.Synchronizer = Base.extend({
            constructor: function(e, t, n) {
                this.editor = e, this.textarea = t, this.composer = n, this._observe()
            },
            fromComposerToTextarea: function(t) {
                this.textarea.setValue(e.lang.string(this.composer.getValue()).trim(), t)
            },
            fromTextareaToComposer: function(e) {
                var t = this.textarea.getValue();
                t ? this.composer.setValue(t, e) : (this.composer.clear(), this.editor.fire("set_placeholder"))
            },
            sync: function(e) {
                "textarea" === this.editor.currentView.name ? this.fromTextareaToComposer(e) : this.fromComposerToTextarea(e)
            },
            _observe: function() {
                var n, i = this,
                    a = this.textarea.element.form,
                    r = function() {
                        n = setInterval(function() {
                            i.fromComposerToTextarea()
                        }, t)
                    },
                    o = function() {
                        clearInterval(n), n = null
                    };
                r(), a && (e.dom.observe(a, "submit", function() {
                    i.sync(!0)
                }), e.dom.observe(a, "reset", function() {
                    setTimeout(function() {
                        i.fromTextareaToComposer()
                    }, 0)
                })), this.editor.observe("change_view", function(e) {
                    "composer" !== e || n ? "textarea" === e && (i.fromComposerToTextarea(!0), o()) : (i.fromTextareaToComposer(!0), r())
                }), this.editor.observe("destroy:composer", o)
            }
        })
    }(wysihtml5), wysihtml5.views.Textarea = wysihtml5.views.View.extend({
        name: "textarea",
        constructor: function(e, t, n) {
            this.base(e, t, n), this._observe()
        },
        clear: function() {
            this.element.value = ""
        },
        getValue: function(e) {
            var t = this.isEmpty() ? "" : this.element.value;
            return e && (t = this.parent.parse(t)), t
        },
        setValue: function(e, t) {
            t && (e = this.parent.parse(e)), this.element.value = e
        },
        hasPlaceholderSet: function() {
            var e = wysihtml5.browser.supportsPlaceholderAttributeOn(this.element),
                t = this.element.getAttribute("placeholder") || null,
                n = this.element.value,
                i = !n;
            return e && i || n === t
        },
        isEmpty: function() {
            return !wysihtml5.lang.string(this.element.value).trim() || this.hasPlaceholderSet()
        },
        _observe: function() {
            var e = this.element,
                t = this.parent,
                n = {
                    focusin: "focus",
                    focusout: "blur"
                },
                i = wysihtml5.browser.supportsEvent("focusin") ? ["focusin", "focusout", "change"] : ["focus", "blur", "change"];
            t.observe("beforeload", function() {
                wysihtml5.dom.observe(e, i, function(e) {
                    var i = n[e.type] || e.type;
                    t.fire(i).fire(i + ":textarea")
                }), wysihtml5.dom.observe(e, ["paste", "drop"], function() {
                    setTimeout(function() {
                        t.fire("paste").fire("paste:textarea")
                    }, 0)
                })
            })
        }
    }),
    function(e) {
        var t = e.dom,
            n = "wysihtml5-command-dialog-opened",
            i = "input, select, textarea",
            a = "[data-wysihtml5-dialog-field]",
            r = "data-wysihtml5-dialog-field";
        e.toolbar.Dialog = e.lang.Dispatcher.extend({
            constructor: function(e, t) {
                this.link = e, this.container = t
            },
            _observe: function() {
                if (!this._observed) {
                    var a = this,
                        r = function(e) {
                            var t = a._serialize();
                            t == a.elementToChange ? a.fire("edit", t) : a.fire("save", t), a.hide(), e.preventDefault(), e.stopPropagation()
                        };
                    t.observe(a.link, "click", function() {
                        t.hasClass(a.link, n) && setTimeout(function() {
                            a.hide()
                        }, 0)
                    }), t.observe(this.container, "keydown", function(t) {
                        var n = t.keyCode;
                        n === e.ENTER_KEY && r(t), n === e.ESCAPE_KEY && a.hide()
                    }), t.delegate(this.container, "[data-wysihtml5-dialog-action=save]", "click", r), t.delegate(this.container, "[data-wysihtml5-dialog-action=cancel]", "click", function(e) {
                        a.fire("cancel"), a.hide(), e.preventDefault(), e.stopPropagation()
                    });
                    for (var o = this.container.querySelectorAll(i), s = 0, d = o.length, l = function() {
                            clearInterval(a.interval)
                        }; d > s; s++) t.observe(o[s], "change", l);
                    this._observed = !0
                }
            },
            _serialize: function() {
                for (var e = this.elementToChange || {}, t = this.container.querySelectorAll(a), n = t.length, i = 0; n > i; i++) e[t[i].getAttribute(r)] = t[i].value;
                return e
            },
            _interpolate: function(e) {
                for (var t, n, i, o = document.querySelector(":focus"), s = this.container.querySelectorAll(a), d = s.length, l = 0; d > l; l++) t = s[l], t !== o && (e && "hidden" === t.type || (n = t.getAttribute(r), i = this.elementToChange ? this.elementToChange[n] || "" : t.defaultValue, t.value = i))
            },
            show: function(e) {
                var a = this,
                    r = this.container.querySelector(i);
                if (this.elementToChange = e, this._observe(), this._interpolate(), e && (this.interval = setInterval(function() {
                        a._interpolate(!0)
                    }, 500)), t.addClass(this.link, n), this.container.style.display = "", this.fire("show"), r && !e) try {
                    r.focus()
                } catch (o) {}
            },
            hide: function() {
                clearInterval(this.interval), this.elementToChange = null, t.removeClass(this.link, n), this.container.style.display = "none", this.fire("hide")
            }
        })
    }(wysihtml5),
    function(e) {
        var t = e.dom,
            n = {
                position: "relative"
            },
            i = {
                left: 0,
                margin: 0,
                opacity: 0,
                overflow: "hidden",
                padding: 0,
                position: "absolute",
                top: 0,
                zIndex: 1
            },
            a = {
                cursor: "inherit",
                fontSize: "50px",
                height: "50px",
                marginTop: "-25px",
                outline: 0,
                padding: 0,
                position: "absolute",
                right: "-4px",
                top: "50%"
            },
            r = {
                "x-webkit-speech": "",
                speech: ""
            };
        e.toolbar.Speech = function(o, s) {
            var d = document.createElement("input");
            if (!e.browser.supportsSpeechApiOn(d)) return s.style.display = "none", void 0;
            var l = document.createElement("div");
            e.lang.object(i).merge({
                width: s.offsetWidth + "px",
                height: s.offsetHeight + "px"
            }), t.insert(d).into(l), t.insert(l).into(s), t.setStyles(a).on(d), t.setAttributes(r).on(d), t.setStyles(i).on(l), t.setStyles(n).on(s);
            var u = "onwebkitspeechchange" in d ? "webkitspeechchange" : "speechchange";
            t.observe(d, u, function() {
                o.execCommand("insertText", d.value), d.value = ""
            }), t.observe(d, "click", function(e) {
                t.hasClass(s, "wysihtml5-command-disabled") && e.preventDefault(), e.stopPropagation()
            })
        }
    }(wysihtml5),
    function(e) {
        var t = "wysihtml5-command-disabled",
            n = "wysihtml5-commands-disabled",
            i = "wysihtml5-command-active",
            a = "wysihtml5-action-active",
            r = e.dom;
        e.toolbar.Toolbar = Base.extend({
            constructor: function(t, n) {
                this.editor = t, this.container = "string" == typeof n ? document.getElementById(n) : n, this.composer = t.composer, this._getLinks("command"), this._getLinks("action"), this._observe(), this.show();
                for (var i = this.container.querySelectorAll("[data-wysihtml5-command=insertSpeech]"), a = i.length, r = 0; a > r; r++) new e.toolbar.Speech(this, i[r])
            },
            _getLinks: function(t) {
                for (var n, i, a, r, o, s = this[t + "Links"] = e.lang.array(this.container.querySelectorAll("[data-wysihtml5-" + t + "]")).get(), d = s.length, l = 0, u = this[t + "Mapping"] = {}; d > l; l++) n = s[l], a = n.getAttribute("data-wysihtml5-" + t), r = n.getAttribute("data-wysihtml5-" + t + "-value"), i = this.container.querySelector("[data-wysihtml5-" + t + "-group='" + a + "']"), o = this._getDialog(n, a), u[a + ":" + r] = {
                    link: n,
                    group: i,
                    name: a,
                    value: r,
                    dialog: o,
                    state: !1
                }
            },
            _getDialog: function(t, n) {
                var i, a, r = this,
                    o = this.container.querySelector("[data-wysihtml5-dialog='" + n + "']");
                return o && (i = new e.toolbar.Dialog(t, o), i.observe("show", function() {
                    a = r.composer.selection.getBookmark(), r.editor.fire("show:dialog", {
                        command: n,
                        dialogContainer: o,
                        commandLink: t
                    })
                }), i.observe("save", function(e) {
                    a && r.composer.selection.setBookmark(a), r._execCommand(n, e), r.editor.fire("save:dialog", {
                        command: n,
                        dialogContainer: o,
                        commandLink: t
                    })
                }), i.observe("cancel", function() {
                    r.editor.focus(!1), r.editor.fire("cancel:dialog", {
                        command: n,
                        dialogContainer: o,
                        commandLink: t
                    })
                })), i
            },
            execCommand: function(e, t) {
                if (!this.commandsDisabled) {
                    var n = this.commandMapping[e + ":" + t];
                    n && n.dialog && !n.state ? n.dialog.show() : this._execCommand(e, t)
                }
            },
            _execCommand: function(e, t) {
                this.editor.focus(!1), this.composer.commands.exec(e, t), this._updateLinkStates()
            },
            execAction: function(e) {
                var t = this.editor;
                switch (e) {
                    case "change_view":
                        t.currentView === t.textarea ? t.fire("change_view", "composer") : t.fire("change_view", "textarea")
                }
            },
            _observe: function() {
                for (var e = this, t = this.editor, i = this.container, a = this.commandLinks.concat(this.actionLinks), o = a.length, s = 0; o > s; s++) r.setAttributes({
                    href: "javascript:;",
                    unselectable: "on"
                }).on(a[s]);
                r.delegate(i, "[data-wysihtml5-command]", "mousedown", function(e) {
                    e.preventDefault()
                }), r.delegate(i, "[data-wysihtml5-command]", "click", function(t) {
                    var n = this,
                        i = n.getAttribute("data-wysihtml5-command"),
                        a = n.getAttribute("data-wysihtml5-command-value");
                    e.execCommand(i, a), t.preventDefault()
                }), r.delegate(i, "[data-wysihtml5-action]", "click", function(t) {
                    var n = this.getAttribute("data-wysihtml5-action");
                    e.execAction(n), t.preventDefault()
                }), t.observe("focus:composer", function() {
                    e.bookmark = null, clearInterval(e.interval), e.interval = setInterval(function() {
                        e._updateLinkStates()
                    }, 500)
                }), t.observe("blur:composer", function() {
                    clearInterval(e.interval)
                }), t.observe("destroy:composer", function() {
                    clearInterval(e.interval)
                }), t.observe("change_view", function(t) {
                    setTimeout(function() {
                        e.commandsDisabled = "composer" !== t, e._updateLinkStates(), e.commandsDisabled ? r.addClass(i, n) : r.removeClass(i, n)
                    }, 0)
                })
            },
            _updateLinkStates: function() {
                var n, o, s, d, l = (this.composer.element, this.commandMapping),
                    u = this.actionMapping;
                for (n in l) d = l[n], this.commandsDisabled ? (o = !1, r.removeClass(d.link, i), d.group && r.removeClass(d.group, i), d.dialog && d.dialog.hide()) : (o = this.composer.commands.state(d.name, d.value), e.lang.object(o).isArray() && (o = 1 === o.length ? o[0] : !0), r.removeClass(d.link, t), d.group && r.removeClass(d.group, t)), d.state !== o && (d.state = o, o ? (r.addClass(d.link, i), d.group && r.addClass(d.group, i), d.dialog && ("object" == typeof o ? d.dialog.show(o) : d.dialog.hide())) : (r.removeClass(d.link, i), d.group && r.removeClass(d.group, i), d.dialog && d.dialog.hide()));
                for (n in u) s = u[n], "change_view" === s.name && (s.state = this.editor.currentView === this.editor.textarea, s.state ? r.addClass(s.link, a) : r.removeClass(s.link, a))
            },
            show: function() {
                this.container.style.display = ""
            },
            hide: function() {
                this.container.style.display = "none"
            }
        })
    }(wysihtml5),
    function(e) {
        var t, n = {
            name: t,
            style: !0,
            toolbar: t,
            autoLink: !0,
            parserRules: {
                tags: {
                    br: {},
                    span: {},
                    div: {},
                    p: {}
                },
                classes: {}
            },
            parser: e.dom.parse,
            composerClassName: "wysihtml5-editor",
            bodyClassName: "wysihtml5-supported",
            stylesheets: [],
            placeholderText: t,
            allowObjectResizing: !0,
            supportTouchDevices: !0
        };
        e.Editor = e.lang.Dispatcher.extend({
            constructor: function(t, i) {
                if (this.textareaElement = "string" == typeof t ? document.getElementById(t) : t, this.config = e.lang.object({}).merge(n).merge(i).get(), this.textarea = new e.views.Textarea(this, this.textareaElement, this.config), this.currentView = this.textarea, this._isCompatible = e.browser.supported(), !this._isCompatible || !this.config.supportTouchDevices && e.browser.isTouchDevice()) {
                    var a = this;
                    return setTimeout(function() {
                        a.fire("beforeload").fire("load")
                    }, 0), void 0
                }
                e.dom.addClass(document.body, this.config.bodyClassName), this.composer = new e.views.Composer(this, this.textareaElement, this.config), this.currentView = this.composer, "function" == typeof this.config.parser && this._initParser(), this.observe("beforeload", function() {
                    this.synchronizer = new e.views.Synchronizer(this, this.textarea, this.composer), this.config.toolbar && (this.toolbar = new e.toolbar.Toolbar(this, this.config.toolbar))
                });
            },
            isCompatible: function() {
                return this._isCompatible
            },
            clear: function() {
                return this.currentView.clear(), this
            },
            getValue: function(e) {
                return this.currentView.getValue(e)
            },
            setValue: function(e, t) {
                return e ? (this.currentView.setValue(e, t), this) : this.clear()
            },
            focus: function(e) {
                return this.currentView.focus(e), this
            },
            disable: function() {
                return this.currentView.disable(), this
            },
            enable: function() {
                return this.currentView.enable(), this
            },
            isEmpty: function() {
                return this.currentView.isEmpty()
            },
            hasPlaceholderSet: function() {
                return this.currentView.hasPlaceholderSet()
            },
            parse: function(t) {
                var n = this.config.parser(t, this.config.parserRules, this.composer.sandbox.getDocument(), !0);
                return "object" == typeof t && e.quirks.redraw(t), n
            },
            _initParser: function() {
                this.observe("paste:composer", function() {
                    var t = !0,
                        n = this;
                    n.composer.selection.executeAndRestore(function() {
                        e.quirks.cleanPastedHTML(n.composer.element), n.parse(n.composer.element)
                    }, t)
                }), this.observe("paste:textarea", function() {
                    var e, t = this.textarea.getValue();
                    e = this.parse(t), this.textarea.setValue(e)
                })
            }
        })
    }(wysihtml5);

/*!
 * @license wysihtml5
 * https://github.com/xing/wysihtml5
 *
 * Author: Christopher Blum (https://github.com/tiff)
 *
 * Copyright (C) 2012 XING AG
 * Licensed under the MIT license (MIT)
 */
! function(t, i) {
    "use strict";
    var a = {
            "font-styles": "<li class='dropdown'><a class='btn btn-default dropdown-toggle' data-toggle='dropdown' href='#'><i class='icon-font'></i>&nbsp;<span class='current-font'>Normal text</span>&nbsp;<b class='caret'></b></a><ul class='dropdown-menu'><li><a data-wysihtml5-command='formatBlock' data-wysihtml5-command-value='div'>Normal text</a></li><li><a data-wysihtml5-command='formatBlock' data-wysihtml5-command-value='h1'>Heading 1</a></li><li><a data-wysihtml5-command='formatBlock' data-wysihtml5-command-value='h2'>Heading 2</a></li></ul></li>",
            emphasis: "<li><div class='btn-group'><a class='btn btn-default' data-wysihtml5-command='bold' title='CTRL+B'>Bold</a><a class='btn btn-default' data-wysihtml5-command='italic' title='CTRL+I'>Italic</a><a class='btn btn-default' data-wysihtml5-command='underline' title='CTRL+U'>Underline</a></div></li>",
            lists: "<li><div class='btn-group'><a class='btn btn-default' data-wysihtml5-command='insertUnorderedList' title='Unordered List'><i class='icon-list'></i></a><a class='btn btn-default' data-wysihtml5-command='insertOrderedList' title='Ordered List'><i class='icon-th-list'></i></a><a class='btn btn-default' data-wysihtml5-command='Outdent' title='Outdent'><i class='icon-indent-right'></i></a><a class='btn btn-default' data-wysihtml5-command='Indent' title='Indent'><i class='icon-indent-left'></i></a></div></li>",
            link: "<li><div class='bootstrap-wysihtml5-insert-link-modal modal fade'><div class='modal-dialog'><div class='modal-content'><div class='modal-header'><a class='close' data-dismiss='modal'>&times;</a><h3>Insert Link</h3></div><div class='modal-body'><input value='http://' class='bootstrap-wysihtml5-insert-link-url form-control'></div><div class='modal-footer'><a href='#' class='btn btn-default' data-dismiss='modal'>Cancel</a><a href='#' class='btn btn-primary' data-dismiss='modal'>Insert link</a></div></div></div></div><a class='btn btn-default' data-wysihtml5-command='createLink' title='Link'><i class='icon-share'></i></a></li>",
            image: "<li><div class='bootstrap-wysihtml5-insert-image-modal modal fade'><div class='modal-dialog'><div class='modal-content'><div class='modal-header'><a class='close' data-dismiss='modal'>&times;</a><h3>Insert Image</h3></div><div class='modal-body'><input value='http://' class='bootstrap-wysihtml5-insert-image-url form-control'></div><div class='modal-footer'><a href='#' class='btn btn-default' data-dismiss='modal'>Cancel</a><a href='#' class='btn btn-primary' data-dismiss='modal'>Insert image</a></div></div></div></div><a class='btn btn-default' data-wysihtml5-command='insertImage' title='Insert image'><i class='icon-picture'></i></a></li>",
            html: "<li><div class='btn-group'><a class='btn btn-default' data-wysihtml5-action='change_view' title='Edit HTML'><i class='icon-pencil'></i></a></div></li>"
        },
        e = {
            "font-styles": !0,
            emphasis: !0,
            lists: !0,
            html: !1,
            link: !0,
            image: !0,
            events: {},
            parserRules: {
                tags: {
                    b: {},
                    i: {},
                    br: {},
                    ol: {},
                    ul: {},
                    li: {},
                    h1: {},
                    h2: {},
                    u: 1,
                    img: {
                        check_attributes: {
                            width: "numbers",
                            alt: "alt",
                            src: "url",
                            height: "numbers"
                        }
                    },
                    a: {
                        set_attributes: {
                            target: "_blank",
                            rel: "nofollow"
                        },
                        check_attributes: {
                            href: "url"
                        }
                    }
                }
            },
            stylesheets: []
        },
        s = function(i, a) {
            this.el = i, this.toolbar = this.createToolbar(i, a || e), this.editor = this.createEditor(a), window.editor = this.editor, t("iframe.wysihtml5-sandbox").each(function(i, a) {
                t(a.contentWindow).off("focus.wysihtml5").on({
                    "focus.wysihtml5": function() {
                        t("li.dropdown").removeClass("open")
                    }
                })
            })
        };
    s.prototype = {
        constructor: s,
        createEditor: function(a) {
            a = t.extend(e, a || {}), a.toolbar = this.toolbar[0];
            var s = new i.Editor(this.el[0], a);
            if (a && a.events)
                for (var n in a.events) s.on(n, a.events[n]);
            return s
        },
        createToolbar: function(i, s) {
            var n = this,
                o = t("<ul/>", {
                    "class": "wysihtml5-toolbar",
                    style: "display:none"
                });
            for (var l in e) {
                var r = !1;
                void 0 !== s[l] ? s[l] === !0 && (r = !0) : r = e[l], r === !0 && (o.append(a[l]), "html" == l && this.initHtml(o), "link" == l && this.initInsertLink(o), "image" == l && this.initInsertImage(o))
            }
            return o.find("a[data-wysihtml5-command='formatBlock']").click(function(i) {
                var a = t(i.srcElement);
                n.toolbar.find(".current-font").text(a.html())
            }), this.el.before(o), o
        },
        initHtml: function(t) {
            var i = "a[data-wysihtml5-action='change_view']";
            t.find(i).click(function() {
                t.find("a.btn").not(i).toggleClass("disabled")
            })
        },
        initInsertImage: function(t) {
            var i = this,
                a = t.find(".bootstrap-wysihtml5-insert-image-modal"),
                e = a.find(".bootstrap-wysihtml5-insert-image-url"),
                s = a.find("a.btn-primary"),
                n = e.val(),
                o = function() {
                    var t = e.val();
                    e.val(n), i.editor.composer.commands.exec("insertImage", t)
                };
            e.keypress(function(t) {
                13 == t.which && (o(), a.modal("hide"))
            }), s.click(o), a.on("shown", function() {
                e.focus()
            }), a.on("hide", function() {
                i.editor.currentView.element.focus()
            }), t.find("a[data-wysihtml5-command=insertImage]").click(function() {
                return a.modal("show"), a.on("click.dismiss.modal", '[data-dismiss="modal"]', function(t) {
                    t.stopPropagation()
                }), !1
            })
        },
        initInsertLink: function(t) {
            var i = this,
                a = t.find(".bootstrap-wysihtml5-insert-link-modal"),
                e = a.find(".bootstrap-wysihtml5-insert-link-url"),
                s = a.find("a.btn-primary"),
                n = e.val(),
                o = function() {
                    var t = e.val();
                    e.val(n), i.editor.composer.commands.exec("createLink", {
                        href: t,
                        target: "_blank",
                        rel: "nofollow"
                    })
                };
            e.keypress(function(t) {
                13 == t.which && (o(), a.modal("hide"))
            }), s.click(o), a.on("shown", function() {
                e.focus()
            }), a.on("hide", function() {
                i.editor.currentView.element.focus()
            }), t.find("a[data-wysihtml5-command=createLink]").click(function() {
                return a.modal("show"), a.on("click.dismiss.modal", '[data-dismiss="modal"]', function(t) {
                    t.stopPropagation()
                }), !1
            })
        }
    }, t.fn.wysihtml5 = function(i) {
        return this.each(function() {
            var a = t(this);
            a.data("wysihtml5", new s(a, i))
        })
    }, t.fn.wysihtml5.Constructor = s
}(window.jQuery, window.wysihtml5);

/*!
 * jQuery Validation Plugin - v1.11.1 - 3/22/2013
 * https://github.com/jzaefferer/jquery-validation
 * Copyright (c) 2013 JΓ¶rn Zaefferer; Licensed MIT
 */
(function(t) {
    t.extend(t.fn, {
        validate: function(e) {
            if (!this.length) return e && e.debug && window.console && console.warn("Nothing selected, can't validate, returning nothing."), void 0;
            var i = t.data(this[0], "validator");
            return i ? i : (this.attr("novalidate", "novalidate"), i = new t.validator(e, this[0]), t.data(this[0], "validator", i), i.settings.onsubmit && (this.validateDelegate(":submit", "click", function(e) {
                i.settings.submitHandler && (i.submitButton = e.target), t(e.target).hasClass("cancel") && (i.cancelSubmit = !0), void 0 !== t(e.target).attr("formnovalidate") && (i.cancelSubmit = !0)
            }), this.submit(function(e) {
                function s() {
                    var s;
                    return i.settings.submitHandler ? (i.submitButton && (s = t("<input type='hidden'/>").attr("name", i.submitButton.name).val(t(i.submitButton).val()).appendTo(i.currentForm)), i.settings.submitHandler.call(i, i.currentForm, e), i.submitButton && s.remove(), !1) : !0
                }
                return i.settings.debug && e.preventDefault(), i.cancelSubmit ? (i.cancelSubmit = !1, s()) : i.form() ? i.pendingRequest ? (i.formSubmitted = !0, !1) : s() : (i.focusInvalid(), !1)
            })), i)
        },
        valid: function() {
            if (t(this[0]).is("form")) return this.validate().form();
            var e = !0,
                i = t(this[0].form).validate();
            return this.each(function() {
                e = e && i.element(this)
            }), e
        },
        removeAttrs: function(e) {
            var i = {},
                s = this;
            return t.each(e.split(/\s/), function(t, e) {
                i[e] = s.attr(e), s.removeAttr(e)
            }), i
        },
        rules: function(e, i) {
            var s = this[0];
            if (e) {
                var r = t.data(s.form, "validator").settings,
                    n = r.rules,
                    a = t.validator.staticRules(s);
                switch (e) {
                    case "add":
                        t.extend(a, t.validator.normalizeRule(i)), delete a.messages, n[s.name] = a, i.messages && (r.messages[s.name] = t.extend(r.messages[s.name], i.messages));
                        break;
                    case "remove":
                        if (!i) return delete n[s.name], a;
                        var u = {};
                        return t.each(i.split(/\s/), function(t, e) {
                            u[e] = a[e], delete a[e]
                        }), u
                }
            }
            var o = t.validator.normalizeRules(t.extend({}, t.validator.classRules(s), t.validator.attributeRules(s), t.validator.dataRules(s), t.validator.staticRules(s)), s);
            if (o.required) {
                var l = o.required;
                delete o.required, o = t.extend({
                    required: l
                }, o)
            }
            return o
        }
    }), t.extend(t.expr[":"], {
        blank: function(e) {
            return !t.trim("" + t(e).val())
        },
        filled: function(e) {
            return !!t.trim("" + t(e).val())
        },
        unchecked: function(e) {
            return !t(e).prop("checked")
        }
    }), t.validator = function(e, i) {
        this.settings = t.extend(!0, {}, t.validator.defaults, e), this.currentForm = i, this.init()
    }, t.validator.format = function(e, i) {
        return 1 === arguments.length ? function() {
            var i = t.makeArray(arguments);
            return i.unshift(e), t.validator.format.apply(this, i)
        } : (arguments.length > 2 && i.constructor !== Array && (i = t.makeArray(arguments).slice(1)), i.constructor !== Array && (i = [i]), t.each(i, function(t, i) {
            e = e.replace(RegExp("\\{" + t + "\\}", "g"), function() {
                return i
            })
        }), e)
    }, t.extend(t.validator, {
        defaults: {
            messages: {},
            groups: {},
            rules: {},
            errorClass: "error",
            validClass: "valid",
            errorElement: "label",
            focusInvalid: !0,
            errorContainer: t([]),
            errorLabelContainer: t([]),
            onsubmit: !0,
            ignore: ":hidden",
            ignoreTitle: !1,
            onfocusin: function(t) {
                this.lastActive = t, this.settings.focusCleanup && !this.blockFocusCleanup && (this.settings.unhighlight && this.settings.unhighlight.call(this, t, this.settings.errorClass, this.settings.validClass), this.addWrapper(this.errorsFor(t)).hide())
            },
            onfocusout: function(t) {
                this.checkable(t) || !(t.name in this.submitted) && this.optional(t) || this.element(t)
            },
            onkeyup: function(t, e) {
                (9 !== e.which || "" !== this.elementValue(t)) && (t.name in this.submitted || t === this.lastElement) && this.element(t)
            },
            onclick: function(t) {
                t.name in this.submitted ? this.element(t) : t.parentNode.name in this.submitted && this.element(t.parentNode)
            },
            highlight: function(e, i, s) {
                "radio" === e.type ? this.findByName(e.name).addClass(i).removeClass(s) : t(e).addClass(i).removeClass(s)
            },
            unhighlight: function(e, i, s) {
                "radio" === e.type ? this.findByName(e.name).removeClass(i).addClass(s) : t(e).removeClass(i).addClass(s)
            }
        },
        setDefaults: function(e) {
            t.extend(t.validator.defaults, e)
        },
        messages: {
            required: "This field is required.",
            remote: "Please fix this field.",
            email: "Please enter a valid email address.",
            url: "Please enter a valid URL.",
            date: "Please enter a valid date.",
            dateISO: "Please enter a valid date (ISO).",
            number: "Please enter a valid number.",
            digits: "Please enter only digits.",
            creditcard: "Please enter a valid credit card number.",
            equalTo: "Please enter the same value again.",
            maxlength: t.validator.format("Please enter no more than {0} characters."),
            minlength: t.validator.format("Please enter at least {0} characters."),
            rangelength: t.validator.format("Please enter a value between {0} and {1} characters long."),
            range: t.validator.format("Please enter a value between {0} and {1}."),
            max: t.validator.format("Please enter a value less than or equal to {0}."),
            min: t.validator.format("Please enter a value greater than or equal to {0}.")
        },
        autoCreateRanges: !1,
        prototype: {
            init: function() {
                function e(e) {
                    var i = t.data(this[0].form, "validator"),
                        s = "on" + e.type.replace(/^validate/, "");
                    i.settings[s] && i.settings[s].call(i, this[0], e)
                }
                this.labelContainer = t(this.settings.errorLabelContainer), this.errorContext = this.labelContainer.length && this.labelContainer || t(this.currentForm), this.containers = t(this.settings.errorContainer).add(this.settings.errorLabelContainer), this.submitted = {}, this.valueCache = {}, this.pendingRequest = 0, this.pending = {}, this.invalid = {}, this.reset();
                var i = this.groups = {};
                t.each(this.settings.groups, function(e, s) {
                    "string" == typeof s && (s = s.split(/\s/)), t.each(s, function(t, s) {
                        i[s] = e
                    })
                });
                var s = this.settings.rules;
                t.each(s, function(e, i) {
                    s[e] = t.validator.normalizeRule(i)
                }), t(this.currentForm).validateDelegate(":text, [type='password'], [type='file'], select, textarea, [type='number'], [type='search'] ,[type='tel'], [type='url'], [type='email'], [type='datetime'], [type='date'], [type='month'], [type='week'], [type='time'], [type='datetime-local'], [type='range'], [type='color'] ", "focusin focusout keyup", e).validateDelegate("[type='radio'], [type='checkbox'], select, option", "click", e), this.settings.invalidHandler && t(this.currentForm).bind("invalid-form.validate", this.settings.invalidHandler)
            },
            form: function() {
                return this.checkForm(), t.extend(this.submitted, this.errorMap), this.invalid = t.extend({}, this.errorMap), this.valid() || t(this.currentForm).triggerHandler("invalid-form", [this]), this.showErrors(), this.valid()
            },
            checkForm: function() {
                this.prepareForm();
                for (var t = 0, e = this.currentElements = this.elements(); e[t]; t++) this.check(e[t]);
                return this.valid()
            },
            element: function(e) {
                e = this.validationTargetFor(this.clean(e)), this.lastElement = e, this.prepareElement(e), this.currentElements = t(e);
                var i = this.check(e) !== !1;
                return i ? delete this.invalid[e.name] : this.invalid[e.name] = !0, this.numberOfInvalids() || (this.toHide = this.toHide.add(this.containers)), this.showErrors(), i
            },
            showErrors: function(e) {
                if (e) {
                    t.extend(this.errorMap, e), this.errorList = [];
                    for (var i in e) this.errorList.push({
                        message: e[i],
                        element: this.findByName(i)[0]
                    });
                    this.successList = t.grep(this.successList, function(t) {
                        return !(t.name in e)
                    })
                }
                this.settings.showErrors ? this.settings.showErrors.call(this, this.errorMap, this.errorList) : this.defaultShowErrors()
            },
            resetForm: function() {
                t.fn.resetForm && t(this.currentForm).resetForm(), this.submitted = {}, this.lastElement = null, this.prepareForm(), this.hideErrors(), this.elements().removeClass(this.settings.errorClass).removeData("previousValue")
            },
            numberOfInvalids: function() {
                return this.objectLength(this.invalid)
            },
            objectLength: function(t) {
                var e = 0;
                for (var i in t) e++;
                return e
            },
            hideErrors: function() {
                this.addWrapper(this.toHide).hide()
            },
            valid: function() {
                return 0 === this.size()
            },
            size: function() {
                return this.errorList.length
            },
            focusInvalid: function() {
                if (this.settings.focusInvalid) try {
                    t(this.findLastActive() || this.errorList.length && this.errorList[0].element || []).filter(":visible").focus().trigger("focusin")
                } catch (e) {}
            },
            findLastActive: function() {
                var e = this.lastActive;
                return e && 1 === t.grep(this.errorList, function(t) {
                    return t.element.name === e.name
                }).length && e
            },
            elements: function() {
                var e = this,
                    i = {};
                return t(this.currentForm).find("input, select, textarea").not(":submit, :reset, :image, [disabled]").not(this.settings.ignore).filter(function() {
                    return !this.name && e.settings.debug && window.console && console.error("%o has no name assigned", this), this.name in i || !e.objectLength(t(this).rules()) ? !1 : (i[this.name] = !0, !0)
                })
            },
            clean: function(e) {
                return t(e)[0]
            },
            errors: function() {
                var e = this.settings.errorClass.replace(" ", ".");
                return t(this.settings.errorElement + "." + e, this.errorContext)
            },
            reset: function() {
                this.successList = [], this.errorList = [], this.errorMap = {}, this.toShow = t([]), this.toHide = t([]), this.currentElements = t([])
            },
            prepareForm: function() {
                this.reset(), this.toHide = this.errors().add(this.containers)
            },
            prepareElement: function(t) {
                this.reset(), this.toHide = this.errorsFor(t)
            },
            elementValue: function(e) {
                var i = t(e).attr("type"),
                    s = t(e).val();
                return "radio" === i || "checkbox" === i ? t("input[name='" + t(e).attr("name") + "']:checked").val() : "string" == typeof s ? s.replace(/\r/g, "") : s
            },
            check: function(e) {
                e = this.validationTargetFor(this.clean(e));
                var i, s = t(e).rules(),
                    r = !1,
                    n = this.elementValue(e);
                for (var a in s) {
                    var u = {
                        method: a,
                        parameters: s[a]
                    };
                    try {
                        if (i = t.validator.methods[a].call(this, n, e, u.parameters), "dependency-mismatch" === i) {
                            r = !0;
                            continue
                        }
                        if (r = !1, "pending" === i) return this.toHide = this.toHide.not(this.errorsFor(e)), void 0;
                        if (!i) return this.formatAndAdd(e, u), !1
                    } catch (o) {
                        throw this.settings.debug && window.console && console.log("Exception occurred when checking element " + e.id + ", check the '" + u.method + "' method.", o), o
                    }
                }
                return r ? void 0 : (this.objectLength(s) && this.successList.push(e), !0)
            },
            customDataMessage: function(e, i) {
                return t(e).data("msg-" + i.toLowerCase()) || e.attributes && t(e).attr("data-msg-" + i.toLowerCase())
            },
            customMessage: function(t, e) {
                var i = this.settings.messages[t];
                return i && (i.constructor === String ? i : i[e])
            },
            findDefined: function() {
                for (var t = 0; arguments.length > t; t++)
                    if (void 0 !== arguments[t]) return arguments[t];
                return void 0
            },
            defaultMessage: function(e, i) {
                return this.findDefined(this.customMessage(e.name, i), this.customDataMessage(e, i), !this.settings.ignoreTitle && e.title || void 0, t.validator.messages[i], "<strong>Warning: No message defined for " + e.name + "</strong>")
            },
            formatAndAdd: function(e, i) {
                var s = this.defaultMessage(e, i.method),
                    r = /\$?\{(\d+)\}/g;
                "function" == typeof s ? s = s.call(this, i.parameters, e) : r.test(s) && (s = t.validator.format(s.replace(r, "{$1}"), i.parameters)), this.errorList.push({
                    message: s,
                    element: e
                }), this.errorMap[e.name] = s, this.submitted[e.name] = s
            },
            addWrapper: function(t) {
                return this.settings.wrapper && (t = t.add(t.parent(this.settings.wrapper))), t
            },
            defaultShowErrors: function() {
                var t, e;
                for (t = 0; this.errorList[t]; t++) {
                    var i = this.errorList[t];
                    this.settings.highlight && this.settings.highlight.call(this, i.element, this.settings.errorClass, this.settings.validClass), this.showLabel(i.element, i.message)
                }
                if (this.errorList.length && (this.toShow = this.toShow.add(this.containers)), this.settings.success)
                    for (t = 0; this.successList[t]; t++) this.showLabel(this.successList[t]);
                if (this.settings.unhighlight)
                    for (t = 0, e = this.validElements(); e[t]; t++) this.settings.unhighlight.call(this, e[t], this.settings.errorClass, this.settings.validClass);
                this.toHide = this.toHide.not(this.toShow), this.hideErrors(), this.addWrapper(this.toShow).show()
            },
            validElements: function() {
                return this.currentElements.not(this.invalidElements())
            },
            invalidElements: function() {
                return t(this.errorList).map(function() {
                    return this.element
                })
            },
            showLabel: function(e, i) {
                var s = this.errorsFor(e);
                s.length ? (s.removeClass(this.settings.validClass).addClass(this.settings.errorClass), s.html(i)) : (s = t("<" + this.settings.errorElement + ">").attr("for", this.idOrName(e)).addClass(this.settings.errorClass).html(i || ""), this.settings.wrapper && (s = s.hide().show().wrap("<" + this.settings.wrapper + "/>").parent()), this.labelContainer.append(s).length || (this.settings.errorPlacement ? this.settings.errorPlacement(s, t(e)) : s.insertAfter(e))), !i && this.settings.success && (s.text(""), "string" == typeof this.settings.success ? s.addClass(this.settings.success) : this.settings.success(s, e)), this.toShow = this.toShow.add(s)
            },
            errorsFor: function(e) {
                var i = this.idOrName(e);
                return this.errors().filter(function() {
                    return t(this).attr("for") === i
                })
            },
            idOrName: function(t) {
                return this.groups[t.name] || (this.checkable(t) ? t.name : t.id || t.name)
            },
            validationTargetFor: function(t) {
                return this.checkable(t) && (t = this.findByName(t.name).not(this.settings.ignore)[0]), t
            },
            checkable: function(t) {
                return /radio|checkbox/i.test(t.type)
            },
            findByName: function(e) {
                return t(this.currentForm).find("[name='" + e + "']")
            },
            getLength: function(e, i) {
                switch (i.nodeName.toLowerCase()) {
                    case "select":
                        return t("option:selected", i).length;
                    case "input":
                        if (this.checkable(i)) return this.findByName(i.name).filter(":checked").length
                }
                return e.length
            },
            depend: function(t, e) {
                return this.dependTypes[typeof t] ? this.dependTypes[typeof t](t, e) : !0
            },
            dependTypes: {
                "boolean": function(t) {
                    return t
                },
                string: function(e, i) {
                    return !!t(e, i.form).length
                },
                "function": function(t, e) {
                    return t(e)
                }
            },
            optional: function(e) {
                var i = this.elementValue(e);
                return !t.validator.methods.required.call(this, i, e) && "dependency-mismatch"
            },
            startRequest: function(t) {
                this.pending[t.name] || (this.pendingRequest++, this.pending[t.name] = !0)
            },
            stopRequest: function(e, i) {
                this.pendingRequest--, 0 > this.pendingRequest && (this.pendingRequest = 0), delete this.pending[e.name], i && 0 === this.pendingRequest && this.formSubmitted && this.form() ? (t(this.currentForm).submit(), this.formSubmitted = !1) : !i && 0 === this.pendingRequest && this.formSubmitted && (t(this.currentForm).triggerHandler("invalid-form", [this]), this.formSubmitted = !1)
            },
            previousValue: function(e) {
                return t.data(e, "previousValue") || t.data(e, "previousValue", {
                    old: null,
                    valid: !0,
                    message: this.defaultMessage(e, "remote")
                })
            }
        },
        classRuleSettings: {
            required: {
                required: !0
            },
            email: {
                email: !0
            },
            url: {
                url: !0
            },
            date: {
                date: !0
            },
            dateISO: {
                dateISO: !0
            },
            number: {
                number: !0
            },
            digits: {
                digits: !0
            },
            creditcard: {
                creditcard: !0
            }
        },
        addClassRules: function(e, i) {
            e.constructor === String ? this.classRuleSettings[e] = i : t.extend(this.classRuleSettings, e)
        },
        classRules: function(e) {
            var i = {},
                s = t(e).attr("class");
            return s && t.each(s.split(" "), function() {
                this in t.validator.classRuleSettings && t.extend(i, t.validator.classRuleSettings[this])
            }), i
        },
        attributeRules: function(e) {
            var i = {},
                s = t(e),
                r = s[0].getAttribute("type");
            for (var n in t.validator.methods) {
                var a;
                "required" === n ? (a = s.get(0).getAttribute(n), "" === a && (a = !0), a = !!a) : a = s.attr(n), /min|max/.test(n) && (null === r || /number|range|text/.test(r)) && (a = Number(a)), a ? i[n] = a : r === n && "range" !== r && (i[n] = !0)
            }
            return i.maxlength && /-1|2147483647|524288/.test(i.maxlength) && delete i.maxlength, i
        },
        dataRules: function(e) {
            var i, s, r = {},
                n = t(e);
            for (i in t.validator.methods) s = n.data("rule-" + i.toLowerCase()), void 0 !== s && (r[i] = s);
            return r
        },
        staticRules: function(e) {
            var i = {},
                s = t.data(e.form, "validator");
            return s.settings.rules && (i = t.validator.normalizeRule(s.settings.rules[e.name]) || {}), i
        },
        normalizeRules: function(e, i) {
            return t.each(e, function(s, r) {
                if (r === !1) return delete e[s], void 0;
                if (r.param || r.depends) {
                    var n = !0;
                    switch (typeof r.depends) {
                        case "string":
                            n = !!t(r.depends, i.form).length;
                            break;
                        case "function":
                            n = r.depends.call(i, i)
                    }
                    n ? e[s] = void 0 !== r.param ? r.param : !0 : delete e[s]
                }
            }), t.each(e, function(s, r) {
                e[s] = t.isFunction(r) ? r(i) : r
            }), t.each(["minlength", "maxlength"], function() {
                e[this] && (e[this] = Number(e[this]))
            }), t.each(["rangelength", "range"], function() {
                var i;
                e[this] && (t.isArray(e[this]) ? e[this] = [Number(e[this][0]), Number(e[this][1])] : "string" == typeof e[this] && (i = e[this].split(/[\s,]+/), e[this] = [Number(i[0]), Number(i[1])]))
            }), t.validator.autoCreateRanges && (e.min && e.max && (e.range = [e.min, e.max], delete e.min, delete e.max), e.minlength && e.maxlength && (e.rangelength = [e.minlength, e.maxlength], delete e.minlength, delete e.maxlength)), e
        },
        normalizeRule: function(e) {
            if ("string" == typeof e) {
                var i = {};
                t.each(e.split(/\s/), function() {
                    i[this] = !0
                }), e = i
            }
            return e
        },
        addMethod: function(e, i, s) {
            t.validator.methods[e] = i, t.validator.messages[e] = void 0 !== s ? s : t.validator.messages[e], 3 > i.length && t.validator.addClassRules(e, t.validator.normalizeRule(e))
        },
        methods: {
            required: function(e, i, s) {
                if (!this.depend(s, i)) return "dependency-mismatch";
                if ("select" === i.nodeName.toLowerCase()) {
                    var r = t(i).val();
                    return r && r.length > 0
                }
                return this.checkable(i) ? this.getLength(e, i) > 0 : t.trim(e).length > 0
            },
            email: function(t, e) {
                return this.optional(e) || /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(t)
            },
            url: function(t, e) {
                return this.optional(e) || /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(t)
            },
            date: function(t, e) {
                return this.optional(e) || !/Invalid|NaN/.test("" + new Date(t))
            },
            dateISO: function(t, e) {
                return this.optional(e) || /^\d{4}[\/\-]\d{1,2}[\/\-]\d{1,2}$/.test(t)
            },
            number: function(t, e) {
                return this.optional(e) || /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(t)
            },
            digits: function(t, e) {
                return this.optional(e) || /^\d+$/.test(t)
            },
            creditcard: function(t, e) {
                if (this.optional(e)) return "dependency-mismatch";
                if (/[^0-9 \-]+/.test(t)) return !1;
                var i = 0,
                    s = 0,
                    r = !1;
                t = t.replace(/\D/g, "");
                for (var n = t.length - 1; n >= 0; n--) {
                    var a = t.charAt(n);
                    s = parseInt(a, 10), r && (s *= 2) > 9 && (s -= 9), i += s, r = !r
                }
                return 0 === i % 10
            },
            minlength: function(e, i, s) {
                var r = t.isArray(e) ? e.length : this.getLength(t.trim(e), i);
                return this.optional(i) || r >= s
            },
            maxlength: function(e, i, s) {
                var r = t.isArray(e) ? e.length : this.getLength(t.trim(e), i);
                return this.optional(i) || s >= r
            },
            rangelength: function(e, i, s) {
                var r = t.isArray(e) ? e.length : this.getLength(t.trim(e), i);
                return this.optional(i) || r >= s[0] && s[1] >= r
            },
            min: function(t, e, i) {
                return this.optional(e) || t >= i
            },
            max: function(t, e, i) {
                return this.optional(e) || i >= t
            },
            range: function(t, e, i) {
                return this.optional(e) || t >= i[0] && i[1] >= t
            },
            equalTo: function(e, i, s) {
                var r = t(s);
                return this.settings.onfocusout && r.unbind(".validate-equalTo").bind("blur.validate-equalTo", function() {
                    t(i).valid()
                }), e === r.val()
            },
            remote: function(e, i, s) {
                if (this.optional(i)) return "dependency-mismatch";
                var r = this.previousValue(i);
                if (this.settings.messages[i.name] || (this.settings.messages[i.name] = {}), r.originalMessage = this.settings.messages[i.name].remote, this.settings.messages[i.name].remote = r.message, s = "string" == typeof s && {
                        url: s
                    } || s, r.old === e) return r.valid;
                r.old = e;
                var n = this;
                this.startRequest(i);
                var a = {};
                return a[i.name] = e, t.ajax(t.extend(!0, {
                    url: s,
                    mode: "abort",
                    port: "validate" + i.name,
                    dataType: "json",
                    data: a,
                    success: function(s) {
                        n.settings.messages[i.name].remote = r.originalMessage;
                        var a = s === !0 || "true" === s;
                        if (a) {
                            var u = n.formSubmitted;
                            n.prepareElement(i), n.formSubmitted = u, n.successList.push(i), delete n.invalid[i.name], n.showErrors()
                        } else {
                            var o = {},
                                l = s || n.defaultMessage(i, "remote");
                            o[i.name] = r.message = t.isFunction(l) ? l(e) : l, n.invalid[i.name] = !0, n.showErrors(o)
                        }
                        r.valid = a, n.stopRequest(i, a)
                    }
                }, s)), "pending"
            }
        }
    }), t.format = t.validator.format
})(jQuery),
function(t) {
    var e = {};
    if (t.ajaxPrefilter) t.ajaxPrefilter(function(t, i, s) {
        var r = t.port;
        "abort" === t.mode && (e[r] && e[r].abort(), e[r] = s)
    });
    else {
        var i = t.ajax;
        t.ajax = function(s) {
            var r = ("mode" in s ? s : t.ajaxSettings).mode,
                n = ("port" in s ? s : t.ajaxSettings).port;
            return "abort" === r ? (e[n] && e[n].abort(), e[n] = i.apply(this, arguments), e[n]) : i.apply(this, arguments)
        }
    }
}(jQuery),
function(t) {
    t.extend(t.fn, {
        validateDelegate: function(e, i, s) {
            return this.bind(i, function(i) {
                var r = t(i.target);
                return r.is(e) ? s.apply(r, arguments) : void 0
            })
        }
    })
}(jQuery);

/*
 * jQuery wizard plug-in 3.0.7 (18-SEPT-2012)
 *
 *
 * Copyright (c) 2012 Jan Sundman (jan.sundman[at]aland.net)
 *
 * http://www.thecodemine.org
 *
 * Licensed under the MIT licens:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 */
(function(e) {
    e.widget("ui.formwizard", {
        _init: function() {
            var t = this;
            var n = this.options.formOptions.success;
            var r = this.options.formOptions.complete;
            var i = this.options.formOptions.beforeSend;
            var s = this.options.formOptions.beforeSubmit;
            var o = this.options.formOptions.beforeSerialize;
            this.options.formOptions = e.extend(this.options.formOptions, {
                success: function(e, r, i) {
                    if (n) {
                        n(e, r, i)
                    }
                    if (t.options.formOptions && t.options.formOptions.resetForm || !t.options.formOptions) {
                        t._reset()
                    }
                },
                complete: function(e, n) {
                    if (r) {
                        r(e, n)
                    }
                    t._enableNavigation()
                },
                beforeSubmit: function(e, n, r) {
                    if (s) {
                        var i = s(e, n, r);
                        if (!i) t._enableNavigation();
                        return i
                    }
                },
                beforeSend: function(e) {
                    if (i) {
                        var n = i(e);
                        if (!n) t._enableNavigation();
                        return n
                    }
                },
                beforeSerialize: function(e, n) {
                    if (o) {
                        var r = o(e, n);
                        if (!r) t._enableNavigation();
                        return r
                    }
                }
            });
            if (this.options.historyEnabled) {
                e.bbq.removeState("_" + e(this.element).attr("id"))
            }
            this.steps = this.element.find(".step").hide();
            this.firstStep = this.steps.eq(0).attr("id");
            this.activatedSteps = new Array;
            this.isLastStep = false;
            this.previousStep = undefined;
            this.currentStep = this.steps.eq(0).attr("id");
            this.nextButton = this.element.find(this.options.next).click(function() {
                return t._next()
            });
            this.nextButtonInitinalValue = this.nextButton.val();
            this.nextButton.val(this.options.textNext);
            this.backButton = this.element.find(this.options.back).click(function() {
                t._back();
                return false
            });
            this.backButtonInitinalValue = this.backButton.val();
            this.backButton.val(this.options.textBack);
            if (this.options.validationEnabled && jQuery().validate == undefined) {
                this.options.validationEnabled = false;
                if (window["console"] !== undefined) {
                    console.log("%s", "validationEnabled option set, but the validation plugin is not included")
                }
            } else if (this.options.validationEnabled) {
                this.element.validate(this.options.validationOptions)
            }
            if (this.options.formPluginEnabled && jQuery().ajaxSubmit == undefined) {
                this.options.formPluginEnabled = false;
                if (window["console"] !== undefined) {
                    console.log("%s", "formPluginEnabled option set but the form plugin is not included")
                }
            }
            if (this.options.disableInputFields == true) {
                e(this.steps).find(":input:not('.wizard-ignore')").attr("disabled", "disabled")
            }
            if (this.options.historyEnabled) {
                e(window).bind("hashchange", undefined, function(n) {
                    var r = n.getState("_" + e(t.element).attr("id")) || t.firstStep;
                    if (r !== t.currentStep) {
                        if (t.options.validationEnabled && r === t._navigate(t.currentStep)) {
                            if (!t.element.valid()) {
                                t._updateHistory(t.currentStep);
                                t.element.validate().focusInvalid();
                                return false
                            }
                        }
                        if (r !== t.currentStep) t._show(r)
                    }
                })
            }
            this.element.addClass("ui-formwizard");
            this.element.find(":input").addClass("ui-wizard-content");
            this.steps.addClass("ui-formwizard-content");
            this.backButton.addClass("ui-formwizard-button ui-wizard-content");
            this.nextButton.addClass("ui-formwizard-button ui-wizard-content");
            if (!this.options.disableUIStyles) {
                this.element.addClass("ui-helper-reset ui-widget ui-widget-content ui-helper-reset ui-corner-all");
                this.element.find(":input").addClass("ui-helper-reset ui-state-default");
                this.steps.addClass("ui-helper-reset ui-corner-all");
                this.backButton.addClass("ui-helper-reset ui-state-default");
                this.nextButton.addClass("ui-helper-reset ui-state-default")
            }
            this._show(undefined);
            return e(this)
        },
        _next: function() {
            if (this.options.validationEnabled) {
                if (!this.element.valid()) {
                    this.element.validate().focusInvalid();
                    return false
                }
            }
            if (this.options.remoteAjax != undefined) {
                var t = this.options.remoteAjax[this.currentStep];
                var n = this;
                if (t !== undefined) {
                    var r = t.success;
                    var i = t.beforeSend;
                    var s = t.complete;
                    t = e.extend({}, t, {
                        success: function(e, t) {
                            if (r !== undefined && r(e, t) || r == undefined) {
                                n._continueToNextStep()
                            }
                        },
                        beforeSend: function(t) {
                            n._disableNavigation();
                            if (i !== undefined) i(t);
                            e(n.element).trigger("before_remote_ajax", {
                                currentStep: n.currentStep
                            })
                        },
                        complete: function(t, r) {
                            if (s !== undefined) s(t, r);
                            e(n.element).trigger("after_remote_ajax", {
                                currentStep: n.currentStep
                            });
                            n._enableNavigation()
                        }
                    });
                    this.element.ajaxSubmit(t);
                    return false
                }
            }
            return this._continueToNextStep()
        },
        _back: function() {
            if (this.activatedSteps.length > 0) {
                if (this.options.historyEnabled) {
                    this._updateHistory(this.activatedSteps[this.activatedSteps.length - 2])
                } else {
                    this._show(this.activatedSteps[this.activatedSteps.length - 2], true)
                }
            }
            return false
        },
        _continueToNextStep: function() {
            if (this.isLastStep) {
                for (var e = 0; e < this.activatedSteps.length; e++) {
                    this.steps.filter("#" + this.activatedSteps[e]).find(":input").not(".wizard-ignore").removeAttr("disabled")
                }
                if (!this.options.formPluginEnabled) {
                    return true
                } else {
                    this._disableNavigation();
                    this.element.ajaxSubmit(this.options.formOptions);
                    return false
                }
            }
            var t = this._navigate(this.currentStep);
            if (t == this.currentStep) {
                return false
            }
            if (this.options.historyEnabled) {
                this._updateHistory(t)
            } else {
                this._show(t, true)
            }
            return false
        },
        _updateHistory: function(t) {
            var n = {};
            n["_" + e(this.element).attr("id")] = t;
            e.bbq.pushState(n)
        },
        _disableNavigation: function() {
            this.nextButton.attr("disabled", "disabled");
            this.backButton.attr("disabled", "disabled");
            if (!this.options.disableUIStyles) {
                this.nextButton.removeClass("ui-state-active").addClass("ui-state-disabled");
                this.backButton.removeClass("ui-state-active").addClass("ui-state-disabled")
            }
        },
        _enableNavigation: function() {
            if (this.isLastStep) {
                this.nextButton.val(this.options.textSubmit)
            } else {
                this.nextButton.val(this.options.textNext)
            }
            if (e.trim(this.currentStep) !== this.steps.eq(0).attr("id")) {
                this.backButton.removeAttr("disabled");
                if (!this.options.disableUIStyles) {
                    this.backButton.removeClass("ui-state-disabled").addClass("ui-state-active")
                }
            }
            this.nextButton.removeAttr("disabled");
            if (!this.options.disableUIStyles) {
                this.nextButton.removeClass("ui-state-disabled").addClass("ui-state-active")
            }
        },
        _animate: function(e, t, n) {
            this._disableNavigation();
            var r = this.steps.filter("#" + e);
            var i = this.steps.filter("#" + t);
            r.find(":input").not(".wizard-ignore").attr("disabled", "disabled");
            i.find(":input").not(".wizard-ignore").removeAttr("disabled");
            var s = this;
            r.animate(s.options.outAnimation, s.options.outDuration, s.options.easing, function() {
                i.animate(s.options.inAnimation, s.options.inDuration, s.options.easing, function() {
                    if (s.options.focusFirstInput) i.find(":input:first").focus();
                    s._enableNavigation();
                    n.apply(s)
                });
                return
            })
        },
        _checkIflastStep: function(t) {
            this.isLastStep = false;
            if (e("#" + t).hasClass(this.options.submitStepClass) || this.steps.filter(":last").attr("id") == t) {
                this.isLastStep = true
            }
        },
        _getLink: function(t) {
            var n = undefined;
            var r = this.steps.filter("#" + t).find(this.options.linkClass);
            if (r != undefined) {
                if (r.filter(":radio,:checkbox").size() > 0) {
                    n = r.filter(this.options.linkClass + ":checked").val()
                } else {
                    n = e(r).val()
                }
            }
            return n
        },
        _navigate: function(e) {
            var t = this._getLink(e);
            if (t != undefined) {
                if (t != "" && t != null && t != undefined && this.steps.filter("#" + t).attr("id") != undefined) {
                    return t
                }
                return this.currentStep
            } else if (t == undefined && !this.isLastStep) {
                var n = this.steps.filter("#" + e).next().attr("id");
                return n
            }
        },
        _show: function(t) {
            var n = false;
            var r = t !== undefined;
            if (t == undefined || t == "") {
                this.activatedSteps.pop();
                t = this.firstStep;
                this.activatedSteps.push(t)
            } else {
                if (e.inArray(t, this.activatedSteps) > -1) {
                    n = true;
                    this.activatedSteps.pop()
                } else {
                    this.activatedSteps.push(t)
                }
            }
            if (this.currentStep !== t || t === this.firstStep) {
                this.previousStep = this.currentStep;
                this._checkIflastStep(t);
                this.currentStep = t;
                var i = function() {
                    if (r) {
                        e(this.element).trigger("step_shown", e.extend({
                            isBackNavigation: n
                        }, this._state()))
                    }
                };
                if (r) {
                    e(this.element).trigger("before_step_shown", e.extend({
                        isBackNavigation: n
                    }, this._state()))
                }
                this._animate(this.previousStep, t, i)
            }
        },
        _reset: function() {
            this.element.resetForm();
            e("label,:input,textarea", this).removeClass("error");
            for (var t = 0; t < this.activatedSteps.length; t++) {
                this.steps.filter("#" + this.activatedSteps[t]).hide().find(":input").attr("disabled", "disabled")
            }
            this.activatedSteps = new Array;
            this.previousStep = undefined;
            this.isLastStep = false;
            if (this.options.historyEnabled) {
                this._updateHistory(this.firstStep)
            } else {
                this._show(this.firstStep)
            }
        },
        _state: function(e) {
            var t = {
                settings: this.options,
                activatedSteps: this.activatedSteps,
                isLastStep: this.isLastStep,
                isFirstStep: this.currentStep === this.firstStep,
                previousStep: this.previousStep,
                currentStep: this.currentStep,
                backButton: this.backButton,
                nextButton: this.nextButton,
                steps: this.steps,
                firstStep: this.firstStep
            };
            if (e !== undefined) return t[e];
            return t
        },
        show: function(e) {
            if (this.options.historyEnabled) {
                this._updateHistory(e)
            } else {
                this._show(e)
            }
        },
        state: function(e) {
            return this._state(e)
        },
        reset: function() {
            this._reset()
        },
        next: function() {
            this._next()
        },
        back: function() {
            this._back()
        },
        destroy: function() {
            this.element.find("*").removeAttr("disabled").show();
            this.nextButton.unbind("click").val(this.nextButtonInitinalValue).removeClass("ui-state-disabled").addClass("ui-state-active");
            this.backButton.unbind("click").val(this.backButtonInitinalValue).removeClass("ui-state-disabled").addClass("ui-state-active");
            this.backButtonInitinalValue = undefined;
            this.nextButtonInitinalValue = undefined;
            this.activatedSteps = undefined;
            this.previousStep = undefined;
            this.currentStep = undefined;
            this.isLastStep = undefined;
            this.options = undefined;
            this.nextButton = undefined;
            this.backButton = undefined;
            this.formwizard = undefined;
            this.element = undefined;
            this.steps = undefined;
            this.firstStep = undefined
        },
        update_steps: function() {
            this.steps = this.element.find(".step").addClass("ui-formwizard-content");
            this.firstStep = this.steps.eq(0).attr("id");
            this.steps.not("#" + this.currentStep).hide().find(":input").addClass("ui-wizard-content").attr("disabled", "disabled");
            this._checkIflastStep(this.currentStep);
            this._enableNavigation();
            if (!this.options.disableUIStyles) {
                this.steps.addClass("ui-helper-reset ui-corner-all");
                this.steps.find(":input").addClass("ui-helper-reset ui-state-default")
            }
        },
        options: {
            historyEnabled: false,
            validationEnabled: false,
            validationOptions: undefined,
            formPluginEnabled: false,
            linkClass: ".link",
            submitStepClass: "submit_step",
            back: ":reset",
            next: ":submit",
            textSubmit: "Submit",
            textNext: "Next",
            textBack: "Back",
            remoteAjax: undefined,
            inAnimation: {
                opacity: "show"
            },
            outAnimation: {
                opacity: "hide"
            },
            inDuration: 400,
            outDuration: 400,
            easing: "swing",
            focusFirstInput: false,
            disableInputFields: true,
            formOptions: {
                reset: true,
                success: function(e) {
                    if (window["console"] !== undefined) {
                        console.log("%s", "form submit successful")
                    }
                },
                disableUIStyles: false
            }
        }
    })
})(jQuery);

/*!
 * Jeditable - jQuery in place edit plugin
 *
 * Copyright (c) 2006-2009 Mika Tuupola, Dylan Verheul
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Project home:
 *   http://www.appelsiini.net/projects/jeditable
 *
 * Based on editable by Dylan Verheul <dylan_at_dyve.net>:
 *    http://www.dyve.net/jquery/?editable
 */
(function($) {
    $.fn.editable = function(e, t) {
        if ("disable" == e) {
            $(this).data("disabled.editable", true);
            return
        }
        if ("enable" == e) {
            $(this).data("disabled.editable", false);
            return
        }
        if ("destroy" == e) {
            $(this).unbind($(this).data("event.editable")).removeData("disabled.editable").removeData("event.editable");
            return
        }
        var n = $.extend({}, $.fn.editable.defaults, {
            target: e
        }, t);
        var r = $.editable.types[n.type].plugin || function() {};
        var i = $.editable.types[n.type].submit || function() {};
        var s = $.editable.types[n.type].buttons || $.editable.types["defaults"].buttons;
        var o = $.editable.types[n.type].content || $.editable.types["defaults"].content;
        var u = $.editable.types[n.type].element || $.editable.types["defaults"].element;
        var a = $.editable.types[n.type].reset || $.editable.types["defaults"].reset;
        var f = n.callback || function() {};
        var l = n.onedit || function() {};
        var c = n.onsubmit || function() {};
        var h = n.onreset || function() {};
        var p = n.onerror || a;
        if (n.tooltip) {
            $(this).attr("title", n.tooltip)
        }
        n.autowidth = "auto" == n.width;
        n.autoheight = "auto" == n.height;
        return this.each(function() {
            var e = this;
            var t = $(e).width();
            var d = $(e).height();
            $(this).data("event.editable", n.event);
            if (!$.trim($(this).html())) {
                $(this).html(n.placeholder)
            }
            $(this).bind(n.event, function(h) {
                if (true === $(this).data("disabled.editable")) {
                    return
                }
                if (e.editing) {
                    return
                }
                if (false === l.apply(this, [n, e])) {
                    return
                }
                h.preventDefault();
                h.stopPropagation();
                if (n.tooltip) {
                    $(e).removeAttr("title")
                }
                if (0 == $(e).width()) {
                    n.width = t;
                    n.height = d
                } else {
                    if (n.width != "none") {
                        n.width = n.autowidth ? $(e).width() : n.width
                    }
                    if (n.height != "none") {
                        n.height = n.autoheight ? $(e).height() : n.height
                    }
                }
                if ($(this).html().toLowerCase().replace(/(;|"|\/)/g, "") == n.placeholder.toLowerCase().replace(/(;|"|\/)/g, "")) {
                    $(this).html("")
                }
                e.editing = true;
                e.revert = $(e).html();
                $(e).html("");
                var v = $("<form />");
                if (n.cssclass) {
                    if ("inherit" == n.cssclass) {
                        v.attr("class", $(e).attr("class"))
                    } else {
                        v.attr("class", n.cssclass)
                    }
                }
                if (n.style) {
                    if ("inherit" == n.style) {
                        v.attr("style", $(e).attr("style"));
                        v.css("display", $(e).css("display"))
                    } else {
                        v.attr("style", n.style)
                    }
                }
                var m = u.apply(v, [n, e]);
                var g;
                if (n.loadurl) {
                    var y = setTimeout(function() {
                        m.disabled = true;
                        o.apply(v, [n.loadtext, n, e])
                    }, 100);
                    var b = {};
                    b[n.id] = e.id;
                    if ($.isFunction(n.loaddata)) {
                        $.extend(b, n.loaddata.apply(e, [e.revert, n]))
                    } else {
                        $.extend(b, n.loaddata)
                    }
                    $.ajax({
                        type: n.loadtype,
                        url: n.loadurl,
                        data: b,
                        async: false,
                        success: function(e) {
                            window.clearTimeout(y);
                            g = e;
                            m.disabled = false
                        }
                    })
                } else if (n.data) {
                    g = n.data;
                    if ($.isFunction(n.data)) {
                        g = n.data.apply(e, [e.revert, n])
                    }
                } else {
                    g = e.revert
                }
                o.apply(v, [g, n, e]);
                m.attr("name", n.name);
                s.apply(v, [n, e]);
                $(e).append(v);
                r.apply(v, [n, e]);
                $(":input:visible:enabled:first", v).focus();
                if (n.select) {
                    m.select()
                }
                m.keydown(function(t) {
                    if (t.keyCode == 27) {
                        t.preventDefault();
                        a.apply(v, [n, e])
                    }
                });
                var y;
                if ("cancel" == n.onblur) {
                    m.blur(function(t) {
                        y = setTimeout(function() {
                            a.apply(v, [n, e])
                        }, 500)
                    })
                } else if ("submit" == n.onblur) {
                    m.blur(function(e) {
                        y = setTimeout(function() {
                            v.submit()
                        }, 200)
                    })
                } else if ($.isFunction(n.onblur)) {
                    m.blur(function(t) {
                        n.onblur.apply(e, [m.val(), n])
                    })
                } else {
                    m.blur(function(e) {})
                }
                v.submit(function(t) {
                    if (y) {
                        clearTimeout(y)
                    }
                    t.preventDefault();
                    if (false !== c.apply(v, [n, e])) {
                        if (false !== i.apply(v, [n, e])) {
                            if ($.isFunction(n.target)) {
                                var r = n.target.apply(e, [m.val(), n]);
                                $(e).html(r);
                                e.editing = false;
                                f.apply(e, [e.innerHTML, n]);
                                if (!$.trim($(e).html())) {
                                    $(e).html(n.placeholder)
                                }
                            } else {
                                var s = {};
                                s[n.name] = m.val();
                                s[n.id] = e.id;
                                if ($.isFunction(n.submitdata)) {
                                    $.extend(s, n.submitdata.apply(e, [e.revert, n]))
                                } else {
                                    $.extend(s, n.submitdata)
                                }
                                if ("PUT" == n.method) {
                                    s["_method"] = "put"
                                }
                                $(e).html(n.indicator);
                                var o = {
                                    type: "POST",
                                    data: s,
                                    dataType: "html",
                                    url: n.target,
                                    success: function(t, r) {
                                        if (o.dataType == "html") {
                                            $(e).html(t)
                                        }
                                        e.editing = false;
                                        f.apply(e, [t, n]);
                                        if (!$.trim($(e).html())) {
                                            $(e).html(n.placeholder)
                                        }
                                    },
                                    error: function(t, r, i) {
                                        p.apply(v, [n, e, t])
                                    }
                                };
                                $.extend(o, n.ajaxoptions);
                                $.ajax(o)
                            }
                        }
                    }
                    $(e).attr("title", n.tooltip);
                    return false
                })
            });
            this.reset = function(t) {
                if (this.editing) {
                    if (false !== h.apply(t, [n, e])) {
                        $(e).html(e.revert);
                        e.editing = false;
                        if (!$.trim($(e).html())) {
                            $(e).html(n.placeholder)
                        }
                        if (n.tooltip) {
                            $(e).attr("title", n.tooltip)
                        }
                    }
                }
            }
        })
    };
    $.editable = {
        types: {
            defaults: {
                element: function(e, t) {
                    var n = $('<input type="hidden"></input>');
                    $(this).append(n);
                    return n
                },
                content: function(e, t, n) {
                    $(":input:first", this).val(e)
                },
                reset: function(e, t) {
                    t.reset(this)
                },
                buttons: function(e, t) {
                    var n = this;
                    if (e.submit) {
                        if (e.submit.match(/>$/)) {
                            var r = $(e.submit).click(function() {
                                if (r.attr("type") != "submit") {
                                    n.submit()
                                }
                            })
                        } else {
                            var r = $('<button type="submit" class="btn btn-xs btn-success"></button>');
                            r.html(e.submit)
                        }
                        $(this).append(r)
                    }
                    if (e.cancel) {
                        if (e.cancel.match(/>$/)) {
                            var i = $(e.cancel)
                        } else {
                            var i = $('<button type="cancel" class="btn btn-xs btn-danger"></button>');
                            i.html(e.cancel)
                        }
                        $(this).append(i);
                        $(i).click(function(r) {
                            if ($.isFunction($.editable.types[e.type].reset)) {
                                var i = $.editable.types[e.type].reset
                            } else {
                                var i = $.editable.types["defaults"].reset
                            }
                            i.apply(n, [e, t]);
                            return false
                        })
                    }
                }
            },
            text: {
                element: function(e, t) {
                    var n = $('<input type="text"/>');
                    if (e.width != "none") {
                        n.attr("width", e.width)
                    }
                    if (e.height != "none") {
                        n.attr("height", e.height)
                    }
                    n.attr("autocomplete", "off");
                    $(this).append(n);
                    return n
                }
            },
            textarea: {
                element: function(e, t) {
                    var n = $("<textarea />");
                    if (e.rows) {
                        n.attr("rows", e.rows)
                    } else if (e.height != "none") {
                        n.height(e.height)
                    }
                    if (e.cols) {
                        n.attr("cols", e.cols)
                    } else if (e.width != "none") {
                        n.width(e.width)
                    }
                    $(this).append(n);
                    return n
                }
            },
            select: {
                element: function(e, t) {
                    var n = $("<select />");
                    $(this).append(n);
                    return n
                },
                content: function(data, settings, original) {
                    if (String == data.constructor) {
                        eval("var json = " + data)
                    } else {
                        var json = data
                    }
                    for (var key in json) {
                        if (!json.hasOwnProperty(key)) {
                            continue
                        }
                        if ("selected" == key) {
                            continue
                        }
                        var option = $("<option />").val(key).append(json[key]);
                        $("select", this).append(option)
                    }
                    $("select", this).children().each(function() {
                        if ($(this).val() == json["selected"] || $(this).text() == $.trim(original.revert)) {
                            $(this).attr("selected", "selected")
                        }
                    });
                    if (!settings.submit) {
                        var form = this;
                        $("select", this).change(function() {
                            form.submit()
                        })
                    }
                }
            }
        },
        addInputType: function(e, t) {
            $.editable.types[e] = t
        }
    };
    $.fn.editable.defaults = {
        name: "value",
        id: "id",
        type: "text",
        width: "auto",
        height: "auto",
        event: "click.editable",
        onblur: "cancel",
        loadtype: "GET",
        loadtext: "Loading...",
        placeholder: "Click to edit",
        loaddata: {},
        submitdata: {},
        ajaxoptions: {}
    }
})(jQuery);

/*!
 * Magnific Popup v0.9.5 by Dmitry Semenov
 *
 * http://bit.ly/magnific-popup#build=inline+image+ajax+iframe+gallery+retina+imagezoom+fastclick
 */
(function(a) {
    var b = "Close",
        c = "BeforeClose",
        d = "AfterClose",
        e = "BeforeAppend",
        f = "MarkupParse",
        g = "Open",
        h = "Change",
        i = "mfp",
        j = "." + i,
        k = "mfp-ready",
        l = "mfp-removing",
        m = "mfp-prevent-close",
        n, o = function() {},
        p = !!window.jQuery,
        q, r = a(window),
        s, t, u, v, w, x = function(a, b) {
            n.ev.on(i + a + j, b)
        },
        y = function(b, c, d, e) {
            var f = document.createElement("div");
            return f.className = "mfp-" + b, d && (f.innerHTML = d), e ? c && c.appendChild(f) : (f = a(f), c && f.appendTo(c)), f
        },
        z = function(b, c) {
            n.ev.triggerHandler(i + b, c), n.st.callbacks && (b = b.charAt(0).toLowerCase() + b.slice(1), n.st.callbacks[b] && n.st.callbacks[b].apply(n, a.isArray(c) ? c : [c]))
        },
        A = function() {
            (n.st.focus ? n.content.find(n.st.focus).eq(0) : n.wrap).focus()
        },
        B = function(b) {
            if (b !== w || !n.currTemplate.closeBtn) n.currTemplate.closeBtn = a(n.st.closeMarkup.replace("%title%", n.st.tClose)), w = b;
            return n.currTemplate.closeBtn
        },
        C = function() {
            a.magnificPopup.instance || (n = new o, n.init(), a.magnificPopup.instance = n)
        },
        D = function(b) {
            if (a(b).hasClass(m)) return;
            var c = n.st.closeOnContentClick,
                d = n.st.closeOnBgClick;
            if (c && d) return !0;
            if (!n.content || a(b).hasClass("mfp-close") || n.preloader && b === n.preloader[0]) return !0;
            if (b !== n.content[0] && !a.contains(n.content[0], b)) {
                if (d && a.contains(document, b)) return !0
            } else if (c) return !0;
            return !1
        },
        E = function() {
            var a = document.createElement("p").style,
                b = ["ms", "O", "Moz", "Webkit"];
            if (a.transition !== undefined) return !0;
            while (b.length)
                if (b.pop() + "Transition" in a) return !0;
            return !1
        };
    o.prototype = {
        constructor: o,
        init: function() {
            var b = navigator.appVersion;
            n.isIE7 = b.indexOf("MSIE 7.") !== -1, n.isIE8 = b.indexOf("MSIE 8.") !== -1, n.isLowIE = n.isIE7 || n.isIE8, n.isAndroid = /android/gi.test(b), n.isIOS = /iphone|ipad|ipod/gi.test(b), n.supportsTransition = E(), n.probablyMobile = n.isAndroid || n.isIOS || /(Opera Mini)|Kindle|webOS|BlackBerry|(Opera Mobi)|(Windows Phone)|IEMobile/i.test(navigator.userAgent), s = a(document.body), t = a(document), n.popupsCache = {}
        },
        open: function(b) {
            var c;
            if (b.isObj === !1) {
                n.items = b.items.toArray(), n.index = 0;
                var d = b.items,
                    e;
                for (c = 0; c < d.length; c++) {
                    e = d[c], e.parsed && (e = e.el[0]);
                    if (e === b.el[0]) {
                        n.index = c;
                        break
                    }
                }
            } else n.items = a.isArray(b.items) ? b.items : [b.items], n.index = b.index || 0;
            if (n.isOpen) {
                n.updateItemHTML();
                return
            }
            n.types = [], v = "", b.mainEl && b.mainEl.length ? n.ev = b.mainEl.eq(0) : n.ev = t, b.key ? (n.popupsCache[b.key] || (n.popupsCache[b.key] = {}), n.currTemplate = n.popupsCache[b.key]) : n.currTemplate = {}, n.st = a.extend(!0, {}, a.magnificPopup.defaults, b), n.fixedContentPos = n.st.fixedContentPos === "auto" ? !n.probablyMobile : n.st.fixedContentPos, n.st.modal && (n.st.closeOnContentClick = !1, n.st.closeOnBgClick = !1, n.st.showCloseBtn = !1, n.st.enableEscapeKey = !1), n.bgOverlay || (n.bgOverlay = y("bg").on("click" + j, function() {
                n.close()
            }), n.wrap = y("wrap").attr("tabindex", -1).on("click" + j, function(a) {
                D(a.target) && n.close()
            }), n.container = y("container", n.wrap)), n.contentContainer = y("content"), n.st.preloader && (n.preloader = y("preloader", n.container, n.st.tLoading));
            var h = a.magnificPopup.modules;
            for (c = 0; c < h.length; c++) {
                var i = h[c];
                i = i.charAt(0).toUpperCase() + i.slice(1), n["init" + i].call(n)
            }
            z("BeforeOpen"), n.st.showCloseBtn && (n.st.closeBtnInside ? (x(f, function(a, b, c, d) {
                c.close_replaceWith = B(d.type)
            }), v += " mfp-close-btn-in") : n.wrap.append(B())), n.st.alignTop && (v += " mfp-align-top"), n.fixedContentPos ? n.wrap.css({
                overflow: n.st.overflowY,
                overflowX: "hidden",
                overflowY: n.st.overflowY
            }) : n.wrap.css({
                top: r.scrollTop(),
                position: "absolute"
            }), (n.st.fixedBgPos === !1 || n.st.fixedBgPos === "auto" && !n.fixedContentPos) && n.bgOverlay.css({
                height: t.height(),
                position: "absolute"
            }), n.st.enableEscapeKey && t.on("keyup" + j, function(a) {
                a.keyCode === 27 && n.close()
            }), r.on("resize" + j, function() {
                n.updateSize()
            }), n.st.closeOnContentClick || (v += " mfp-auto-cursor"), v && n.wrap.addClass(v);
            var l = n.wH = r.height(),
                m = {};
            if (n.fixedContentPos && n._hasScrollBar(l)) {
                var o = n._getScrollbarSize();
                o && (m.paddingRight = o)
            }
            n.fixedContentPos && (n.isIE7 ? a("body, html").css("overflow", "hidden") : m.overflow = "hidden");
            var p = n.st.mainClass;
            n.isIE7 && (p += " mfp-ie7"), p && n._addClassToMFP(p), n.updateItemHTML(), z("BuildControls"), a("html").css(m), n.bgOverlay.add(n.wrap).prependTo(document.body), n._lastFocusedEl = document.activeElement, setTimeout(function() {
                n.content ? (n._addClassToMFP(k), A()) : n.bgOverlay.addClass(k), t.on("focusin" + j, function(b) {
                    if (b.target !== n.wrap[0] && !a.contains(n.wrap[0], b.target)) return A(), !1
                })
            }, 16), n.isOpen = !0, n.updateSize(l), z(g)
        },
        close: function() {
            if (!n.isOpen) return;
            z(c), n.isOpen = !1, n.st.removalDelay && !n.isLowIE && n.supportsTransition ? (n._addClassToMFP(l), setTimeout(function() {
                n._close()
            }, n.st.removalDelay)) : n._close()
        },
        _close: function() {
            z(b);
            var c = l + " " + k + " ";
            n.bgOverlay.detach(), n.wrap.detach(), n.container.empty(), n.st.mainClass && (c += n.st.mainClass + " "), n._removeClassFromMFP(c);
            if (n.fixedContentPos) {
                var e = {
                    paddingRight: ""
                };
                n.isIE7 ? a("body, html").css("overflow", "") : e.overflow = "", a("html").css(e)
            }
            t.off("keyup" + j + " focusin" + j), n.ev.off(j), n.wrap.attr("class", "mfp-wrap").removeAttr("style"), n.bgOverlay.attr("class", "mfp-bg"), n.container.attr("class", "mfp-container"), n.st.showCloseBtn && (!n.st.closeBtnInside || n.currTemplate[n.currItem.type] === !0) && n.currTemplate.closeBtn && n.currTemplate.closeBtn.detach(), n._lastFocusedEl && a(n._lastFocusedEl).focus(), n.currItem = null, n.content = null, n.currTemplate = null, n.prevHeight = 0, z(d)
        },
        updateSize: function(a) {
            if (n.isIOS) {
                var b = document.documentElement.clientWidth / window.innerWidth,
                    c = window.innerHeight * b;
                n.wrap.css("height", c), n.wH = c
            } else n.wH = a || r.height();
            n.fixedContentPos || n.wrap.css("height", n.wH), z("Resize")
        },
        updateItemHTML: function() {
            var b = n.items[n.index];
            n.contentContainer.detach(), n.content && n.content.detach(), b.parsed || (b = n.parseEl(n.index));
            var c = b.type;
            z("BeforeChange", [n.currItem ? n.currItem.type : "", c]), n.currItem = b;
            if (!n.currTemplate[c]) {
                var d = n.st[c] ? n.st[c].markup : !1;
                z("FirstMarkupParse", d), d ? n.currTemplate[c] = a(d) : n.currTemplate[c] = !0
            }
            u && u !== b.type && n.container.removeClass("mfp-" + u + "-holder");
            var e = n["get" + c.charAt(0).toUpperCase() + c.slice(1)](b, n.currTemplate[c]);
            n.appendContent(e, c), b.preloaded = !0, z(h, b), u = b.type, n.container.prepend(n.contentContainer), z("AfterChange")
        },
        appendContent: function(a, b) {
            n.content = a, a ? n.st.showCloseBtn && n.st.closeBtnInside && n.currTemplate[b] === !0 ? n.content.find(".mfp-close").length || n.content.append(B()) : n.content = a : n.content = "", z(e), n.container.addClass("mfp-" + b + "-holder"), n.contentContainer.append(n.content)
        },
        parseEl: function(b) {
            var c = n.items[b],
                d = c.type;
            c.tagName ? c = {
                el: a(c)
            } : c = {
                data: c,
                src: c.src
            };
            if (c.el) {
                var e = n.types;
                for (var f = 0; f < e.length; f++)
                    if (c.el.hasClass("mfp-" + e[f])) {
                        d = e[f];
                        break
                    }
                c.src = c.el.attr("data-mfp-src"), c.src || (c.src = c.el.attr("href"))
            }
            return c.type = d || n.st.type || "inline", c.index = b, c.parsed = !0, n.items[b] = c, z("ElementParse", c), n.items[b]
        },
        addGroup: function(a, b) {
            var c = function(c) {
                c.mfpEl = this, n._openClick(c, a, b)
            };
            b || (b = {});
            var d = "click.magnificPopup";
            b.mainEl = a, b.items ? (b.isObj = !0, a.off(d).on(d, c)) : (b.isObj = !1, b.delegate ? a.off(d).on(d, b.delegate, c) : (b.items = a, a.off(d).on(d, c)))
        },
        _openClick: function(b, c, d) {
            var e = d.midClick !== undefined ? d.midClick : a.magnificPopup.defaults.midClick;
            if (!e && (b.which === 2 || b.ctrlKey || b.metaKey)) return;
            var f = d.disableOn !== undefined ? d.disableOn : a.magnificPopup.defaults.disableOn;
            if (f)
                if (a.isFunction(f)) {
                    if (!f.call(n)) return !0
                } else if (r.width() < f) return !0;
            b.type && (b.preventDefault(), n.isOpen && b.stopPropagation()), d.el = a(b.mfpEl), d.delegate && (d.items = c.find(d.delegate)), n.open(d)
        },
        updateStatus: function(a, b) {
            if (n.preloader) {
                q !== a && n.container.removeClass("mfp-s-" + q), !b && a === "loading" && (b = n.st.tLoading);
                var c = {
                    status: a,
                    text: b
                };
                z("UpdateStatus", c), a = c.status, b = c.text, n.preloader.html(b), n.preloader.find("a").on("click", function(a) {
                    a.stopImmediatePropagation()
                }), n.container.addClass("mfp-s-" + a), q = a
            }
        },
        _addClassToMFP: function(a) {
            n.bgOverlay.addClass(a), n.wrap.addClass(a)
        },
        _removeClassFromMFP: function(a) {
            this.bgOverlay.removeClass(a), n.wrap.removeClass(a)
        },
        _hasScrollBar: function(a) {
            return (n.isIE7 ? t.height() : document.body.scrollHeight) > (a || r.height())
        },
        _parseMarkup: function(b, c, d) {
            var e;
            d.data && (c = a.extend(d.data, c)), z(f, [b, c, d]), a.each(c, function(a, c) {
                if (c === undefined || c === !1) return !0;
                e = a.split("_");
                if (e.length > 1) {
                    var d = b.find(j + "-" + e[0]);
                    if (d.length > 0) {
                        var f = e[1];
                        f === "replaceWith" ? d[0] !== c[0] && d.replaceWith(c) : f === "img" ? d.is("img") ? d.attr("src", c) : d.replaceWith('<img src="' + c + '" class="' + d.attr("class") + '" />') : d.attr(e[1], c)
                    }
                } else b.find(j + "-" + a).html(c)
            })
        },
        _getScrollbarSize: function() {
            if (n.scrollbarSize === undefined) {
                var a = document.createElement("div");
                a.id = "mfp-sbm", a.style.cssText = "width: 99px; height: 99px; overflow: scroll; position: absolute; top: -9999px;", document.body.appendChild(a), n.scrollbarSize = a.offsetWidth - a.clientWidth, document.body.removeChild(a)
            }
            return n.scrollbarSize
        }
    }, a.magnificPopup = {
        instance: null,
        proto: o.prototype,
        modules: [],
        open: function(a, b) {
            return C(), a || (a = {}), a.isObj = !0, a.index = b || 0, this.instance.open(a)
        },
        close: function() {
            return a.magnificPopup.instance.close()
        },
        registerModule: function(b, c) {
            c.options && (a.magnificPopup.defaults[b] = c.options), a.extend(this.proto, c.proto), this.modules.push(b)
        },
        defaults: {
            disableOn: 0,
            key: null,
            midClick: !1,
            mainClass: "",
            preloader: !0,
            focus: "",
            closeOnContentClick: !1,
            closeOnBgClick: !0,
            closeBtnInside: !0,
            showCloseBtn: !0,
            enableEscapeKey: !0,
            modal: !1,
            alignTop: !1,
            removalDelay: 0,
            fixedContentPos: "auto",
            fixedBgPos: "auto",
            overflowY: "auto",
            closeMarkup: '<button title="%title%" type="button" class="mfp-close">&times;</button>',
            tClose: "Close (Esc)",
            tLoading: "Loading..."
        }
    }, a.fn.magnificPopup = function(b) {
        C();
        var c = a(this);
        if (typeof b == "string")
            if (b === "open") {
                var d, e = p ? c.data("magnificPopup") : c[0].magnificPopup,
                    f = parseInt(arguments[1], 10) || 0;
                e.items ? d = e.items[f] : (d = c, e.delegate && (d = d.find(e.delegate)), d = d.eq(f)), n._openClick({
                    mfpEl: d
                }, c, e)
            } else n.isOpen && n[b].apply(n, Array.prototype.slice.call(arguments, 1));
        else p ? c.data("magnificPopup", b) : c[0].magnificPopup = b, n.addGroup(c, b);
        return c
    };
    var F = "inline",
        G, H, I, J = function() {
            I && (H.after(I.addClass(G)).detach(), I = null)
        };
    a.magnificPopup.registerModule(F, {
        options: {
            hiddenClass: "hide",
            markup: "",
            tNotFound: "Content not found"
        },
        proto: {
            initInline: function() {
                n.types.push(F), x(b + "." + F, function() {
                    J()
                })
            },
            getInline: function(b, c) {
                J();
                if (b.src) {
                    var d = n.st.inline,
                        e = a(b.src);
                    if (e.length) {
                        var f = e[0].parentNode;
                        f && f.tagName && (H || (G = d.hiddenClass, H = y(G), G = "mfp-" + G), I = e.after(H).detach().removeClass(G)), n.updateStatus("ready")
                    } else n.updateStatus("error", d.tNotFound), e = a("<div>");
                    return b.inlineElement = e, e
                }
                return n.updateStatus("ready"), n._parseMarkup(c, {}, b), c
            }
        }
    });
    var K = "ajax",
        L, M = function() {
            L && s.removeClass(L)
        };
    a.magnificPopup.registerModule(K, {
        options: {
            settings: null,
            cursor: "mfp-ajax-cur",
            tError: '<a href="%url%">The content</a> could not be loaded.'
        },
        proto: {
            initAjax: function() {
                n.types.push(K), L = n.st.ajax.cursor, x(b + "." + K, function() {
                    M(), n.req && n.req.abort()
                })
            },
            getAjax: function(b) {
                L && s.addClass(L), n.updateStatus("loading");
                var c = a.extend({
                    url: b.src,
                    success: function(c, d, e) {
                        var f = {
                            data: c,
                            xhr: e
                        };
                        z("ParseAjax", f), n.appendContent(a(f.data), K), b.finished = !0, M(), A(), setTimeout(function() {
                            n.wrap.addClass(k)
                        }, 16), n.updateStatus("ready"), z("AjaxContentAdded")
                    },
                    error: function() {
                        M(), b.finished = b.loadError = !0, n.updateStatus("error", n.st.ajax.tError.replace("%url%", b.src))
                    }
                }, n.st.ajax.settings);
                return n.req = a.ajax(c), ""
            }
        }
    });
    var N, O = function(b) {
        if (b.data && b.data.title !== undefined) return b.data.title;
        var c = n.st.image.titleSrc;
        if (c) {
            if (a.isFunction(c)) return c.call(n, b);
            if (b.el) return b.el.attr(c) || ""
        }
        return ""
    };
    a.magnificPopup.registerModule("image", {
        options: {
            markup: '<div class="mfp-figure"><div class="mfp-close"></div><div class="mfp-img"></div><div class="mfp-bottom-bar"><div class="mfp-title"></div><div class="mfp-counter"></div></div></div>',
            cursor: "mfp-zoom-out-cur",
            titleSrc: "title",
            verticalFit: !0,
            tError: '<a href="%url%">The image</a> could not be loaded.'
        },
        proto: {
            initImage: function() {
                var a = n.st.image,
                    c = ".image";
                n.types.push("image"), x(g + c, function() {
                    n.currItem.type === "image" && a.cursor && s.addClass(a.cursor)
                }), x(b + c, function() {
                    a.cursor && s.removeClass(a.cursor), r.off("resize" + j)
                }), x("Resize" + c, n.resizeImage), n.isLowIE && x("AfterChange", n.resizeImage)
            },
            resizeImage: function() {
                var a = n.currItem;
                if (!a || !a.img) return;
                if (n.st.image.verticalFit) {
                    var b = 0;
                    n.isLowIE && (b = parseInt(a.img.css("padding-top"), 10) + parseInt(a.img.css("padding-bottom"), 10)), a.img.css("max-height", n.wH - b)
                }
            },
            _onImageHasSize: function(a) {
                a.img && (a.hasSize = !0, N && clearInterval(N), a.isCheckingImgSize = !1, z("ImageHasSize", a), a.imgHidden && (n.content && n.content.removeClass("mfp-loading"), a.imgHidden = !1))
            },
            findImageSize: function(a) {
                var b = 0,
                    c = a.img[0],
                    d = function(e) {
                        N && clearInterval(N), N = setInterval(function() {
                            if (c.naturalWidth > 0) {
                                n._onImageHasSize(a);
                                return
                            }
                            b > 200 && clearInterval(N), b++, b === 3 ? d(10) : b === 40 ? d(50) : b === 100 && d(500)
                        }, e)
                    };
                d(1)
            },
            getImage: function(b, c) {
                var d = 0,
                    e = function() {
                        b && (b.img[0].complete ? (b.img.off(".mfploader"), b === n.currItem && (n._onImageHasSize(b), n.updateStatus("ready")), b.hasSize = !0, b.loaded = !0, z("ImageLoadComplete")) : (d++, d < 200 ? setTimeout(e, 100) : f()))
                    },
                    f = function() {
                        b && (b.img.off(".mfploader"), b === n.currItem && (n._onImageHasSize(b), n.updateStatus("error", g.tError.replace("%url%", b.src))), b.hasSize = !0, b.loaded = !0, b.loadError = !0)
                    },
                    g = n.st.image,
                    h = c.find(".mfp-img");
                if (h.length) {
                    var i = document.createElement("img");
                    i.className = "mfp-img", b.img = a(i).on("load.mfploader", e).on("error.mfploader", f), i.src = b.src, h.is("img") && (b.img = b.img.clone()), b.img[0].naturalWidth > 0 && (b.hasSize = !0)
                }
                return n._parseMarkup(c, {
                    title: O(b),
                    img_replaceWith: b.img
                }, b), n.resizeImage(), b.hasSize ? (N && clearInterval(N), b.loadError ? (c.addClass("mfp-loading"), n.updateStatus("error", g.tError.replace("%url%", b.src))) : (c.removeClass("mfp-loading"), n.updateStatus("ready")), c) : (n.updateStatus("loading"), b.loading = !0, b.hasSize || (b.imgHidden = !0, c.addClass("mfp-loading"), n.findImageSize(b)), c)
            }
        }
    });
    var P, Q = function() {
        return P === undefined && (P = document.createElement("p").style.MozTransform !== undefined), P
    };
    a.magnificPopup.registerModule("zoom", {
        options: {
            enabled: !1,
            easing: "ease-in-out",
            duration: 300,
            opener: function(a) {
                return a.is("img") ? a : a.find("img")
            }
        },
        proto: {
            initZoom: function() {
                var a = n.st.zoom,
                    d = ".zoom";
                if (!a.enabled || !n.supportsTransition) return;
                var e = a.duration,
                    f = function(b) {
                        var c = b.clone().removeAttr("style").removeAttr("class").addClass("mfp-animated-image"),
                            d = "all " + a.duration / 1e3 + "s " + a.easing,
                            e = {
                                position: "fixed",
                                zIndex: 9999,
                                left: 0,
                                top: 0,
                                "-webkit-backface-visibility": "hidden"
                            },
                            f = "transition";
                        return e["-webkit-" + f] = e["-moz-" + f] = e["-o-" + f] = e[f] = d, c.css(e), c
                    },
                    g = function() {
                        n.content.css("visibility", "visible")
                    },
                    h, i;
                x("BuildControls" + d, function() {
                    if (n._allowZoom()) {
                        clearTimeout(h), n.content.css("visibility", "hidden"), image = n._getItemToZoom();
                        if (!image) {
                            g();
                            return
                        }
                        i = f(image), i.css(n._getOffset()), n.wrap.append(i), h = setTimeout(function() {
                            i.css(n._getOffset(!0)), h = setTimeout(function() {
                                g(), setTimeout(function() {
                                    i.remove(), image = i = null, z("ZoomAnimationEnded")
                                }, 16)
                            }, e)
                        }, 16)
                    }
                }), x(c + d, function() {
                    if (n._allowZoom()) {
                        clearTimeout(h), n.st.removalDelay = e;
                        if (!image) {
                            image = n._getItemToZoom();
                            if (!image) return;
                            i = f(image)
                        }
                        i.css(n._getOffset(!0)), n.wrap.append(i), n.content.css("visibility", "hidden"), setTimeout(function() {
                            i.css(n._getOffset())
                        }, 16)
                    }
                }), x(b + d, function() {
                    n._allowZoom() && (g(), i && i.remove())
                })
            },
            _allowZoom: function() {
                return n.currItem.type === "image"
            },
            _getItemToZoom: function() {
                return n.currItem.hasSize ? n.currItem.img : !1
            },
            _getOffset: function(b) {
                var c;
                b ? c = n.currItem.img : c = n.st.zoom.opener(n.currItem.el || n.currItem);
                var d = c.offset(),
                    e = parseInt(c.css("padding-top"), 10),
                    f = parseInt(c.css("padding-bottom"), 10);
                d.top -= a(window).scrollTop() - e;
                var g = {
                    width: c.width(),
                    height: (p ? c.innerHeight() : c[0].offsetHeight) - f - e
                };
                return Q() ? g["-moz-transform"] = g.transform = "translate(" + d.left + "px," + d.top + "px)" : (g.left = d.left, g.top = d.top), g
            }
        }
    });
    var R = "iframe",
        S = "//about:blank",
        T = function(a) {
            if (n.currTemplate[R]) {
                var b = n.currTemplate[R].find("iframe");
                b.length && (a || (b[0].src = S), n.isIE8 && b.css("display", a ? "block" : "none"))
            }
        };
    a.magnificPopup.registerModule(R, {
        options: {
            markup: '<div class="mfp-iframe-scaler"><div class="mfp-close"></div><iframe class="mfp-iframe" src="//about:blank" frameborder="0" allowfullscreen></iframe></div>',
            srcAction: "iframe_src",
            patterns: {
                youtube: {
                    index: "youtube.com",
                    id: "v=",
                    src: "//www.youtube.com/embed/%id%?autoplay=1"
                },
                vimeo: {
                    index: "vimeo.com/",
                    id: "/",
                    src: "//player.vimeo.com/video/%id%?autoplay=1"
                },
                gmaps: {
                    index: "//maps.google.",
                    src: "%id%&output=embed"
                }
            }
        },
        proto: {
            initIframe: function() {
                n.types.push(R), x("BeforeChange", function(a, b, c) {
                    b !== c && (b === R ? T() : c === R && T(!0))
                }), x(b + "." + R, function() {
                    T()
                })
            },
            getIframe: function(b, c) {
                var d = b.src,
                    e = n.st.iframe;
                a.each(e.patterns, function() {
                    if (d.indexOf(this.index) > -1) return this.id && (typeof this.id == "string" ? d = d.substr(d.lastIndexOf(this.id) + this.id.length, d.length) : d = this.id.call(this, d)), d = this.src.replace("%id%", d), !1
                });
                var f = {};
                return e.srcAction && (f[e.srcAction] = d), n._parseMarkup(c, f, b), n.updateStatus("ready"), c
            }
        }
    });
    var U = function(a) {
            var b = n.items.length;
            return a > b - 1 ? a - b : a < 0 ? b + a : a
        },
        V = function(a, b, c) {
            return a.replace("%curr%", b + 1).replace("%total%", c)
        };
    a.magnificPopup.registerModule("gallery", {
        options: {
            enabled: !1,
            arrowMarkup: '<button title="%title%" type="button" class="mfp-arrow mfp-arrow-%dir%"></button>',
            preload: [0, 2],
            navigateByImgClick: !0,
            arrows: !0,
            tPrev: "Previous (Left arrow key)",
            tNext: "Next (Right arrow key)",
            tCounter: "%curr% of %total%"
        },
        proto: {
            initGallery: function() {
                var c = n.st.gallery,
                    d = ".mfp-gallery",
                    e = Boolean(a.fn.mfpFastClick);
                n.direction = !0;
                if (!c || !c.enabled) return !1;
                v += " mfp-gallery", x(g + d, function() {
                    c.navigateByImgClick && n.wrap.on("click" + d, ".mfp-img", function() {
                        if (n.items.length > 1) return n.next(), !1
                    }), t.on("keydown" + d, function(a) {
                        a.keyCode === 37 ? n.prev() : a.keyCode === 39 && n.next()
                    })
                }), x("UpdateStatus" + d, function(a, b) {
                    b.text && (b.text = V(b.text, n.currItem.index, n.items.length))
                }), x(f + d, function(a, b, d, e) {
                    var f = n.items.length;
                    d.counter = f > 1 ? V(c.tCounter, e.index, f) : ""
                }), x("BuildControls" + d, function() {
                    if (n.items.length > 1 && c.arrows && !n.arrowLeft) {
                        var b = c.arrowMarkup,
                            d = n.arrowLeft = a(b.replace("%title%", c.tPrev).replace("%dir%", "left")).addClass(m),
                            f = n.arrowRight = a(b.replace("%title%", c.tNext).replace("%dir%", "right")).addClass(m),
                            g = e ? "mfpFastClick" : "click";
                        d[g](function() {
                            n.prev()
                        }), f[g](function() {
                            n.next()
                        }), n.isIE7 && (y("b", d[0], !1, !0), y("a", d[0], !1, !0), y("b", f[0], !1, !0), y("a", f[0], !1, !0)), n.container.append(d.add(f))
                    }
                }), x(h + d, function() {
                    n._preloadTimeout && clearTimeout(n._preloadTimeout), n._preloadTimeout = setTimeout(function() {
                        n.preloadNearbyImages(), n._preloadTimeout = null
                    }, 16)
                }), x(b + d, function() {
                    t.off(d), n.wrap.off("click" + d), n.arrowLeft && e && n.arrowLeft.add(n.arrowRight).destroyMfpFastClick(), n.arrowRight = n.arrowLeft = null
                })
            },
            next: function() {
                n.direction = !0, n.index = U(n.index + 1), n.updateItemHTML()
            },
            prev: function() {
                n.direction = !1, n.index = U(n.index - 1), n.updateItemHTML()
            },
            goTo: function(a) {
                n.direction = a >= n.index, n.index = a, n.updateItemHTML()
            },
            preloadNearbyImages: function() {
                var a = n.st.gallery.preload,
                    b = Math.min(a[0], n.items.length),
                    c = Math.min(a[1], n.items.length),
                    d;
                for (d = 1; d <= (n.direction ? c : b); d++) n._preloadItem(n.index + d);
                for (d = 1; d <= (n.direction ? b : c); d++) n._preloadItem(n.index - d)
            },
            _preloadItem: function(b) {
                b = U(b);
                if (n.items[b].preloaded) return;
                var c = n.items[b];
                c.parsed || (c = n.parseEl(b)), z("LazyLoad", c), c.type === "image" && (c.img = a('<img class="mfp-img" />').on("load.mfploader", function() {
                    c.hasSize = !0
                }).on("error.mfploader", function() {
                    c.hasSize = !0, c.loadError = !0, z("LazyLoadError", c)
                }).attr("src", c.src)), c.preloaded = !0
            }
        }
    });
    var W = "retina";
    a.magnificPopup.registerModule(W, {
            options: {
                replaceSrc: function(a) {
                    return a.src.replace(/\.\w+$/, function(a) {
                        return "@2x" + a
                    })
                },
                ratio: 1
            },
            proto: {
                initRetina: function() {
                    if (window.devicePixelRatio > 1) {
                        var a = n.st.retina,
                            b = a.ratio;
                        b = isNaN(b) ? b() : b, b > 1 && (x("ImageHasSize." + W, function(a, c) {
                            c.img.css({
                                "max-width": c.img[0].naturalWidth / b,
                                width: "100%"
                            })
                        }), x("ElementParse." + W, function(c, d) {
                            d.src = a.replaceSrc(d, b)
                        }))
                    }
                }
            }
        }),
        function() {
            var b = 1e3,
                c = "ontouchstart" in window,
                d = function() {
                    r.off("touchmove" + f + " touchend" + f)
                },
                e = "mfpFastClick",
                f = "." + e;
            a.fn.mfpFastClick = function(e) {
                return a(this).each(function() {
                    var g = a(this),
                        h;
                    if (c) {
                        var i, j, k, l, m, n;
                        g.on("touchstart" + f, function(a) {
                            l = !1, n = 1, m = a.originalEvent ? a.originalEvent.touches[0] : a.touches[0], j = m.clientX, k = m.clientY, r.on("touchmove" + f, function(a) {
                                m = a.originalEvent ? a.originalEvent.touches : a.touches, n = m.length, m = m[0];
                                if (Math.abs(m.clientX - j) > 10 || Math.abs(m.clientY - k) > 10) l = !0, d()
                            }).on("touchend" + f, function(a) {
                                d();
                                if (l || n > 1) return;
                                h = !0, a.preventDefault(), clearTimeout(i), i = setTimeout(function() {
                                    h = !1
                                }, b), e()
                            })
                        })
                    }
                    g.on("click" + f, function() {
                        h || e()
                    })
                })
            }, a.fn.destroyMfpFastClick = function() {
                a(this).off("touchstart" + f + " click" + f), c && r.off("touchmove" + f + " touchend" + f)
            }
        }()
})(window.jQuery || window.Zepto);

/*!
 * Prism: Lightweight, robust, elegant syntax highlighting
 * MIT license http://www.opensource.org/licenses/mit-license.php/
 * @author Lea Verou http://lea.verou.me
 */
(function() {
    var e = /\blang(?:uage)?-(?!\*)(\w+)\b/i,
        t = self.Prism = {
            util: {
                type: function(e) {
                    return Object.prototype.toString.call(e).match(/\[object (\w+)\]/)[1]
                },
                clone: function(e) {
                    var n = t.util.type(e);
                    switch (n) {
                        case "Object":
                            var r = {};
                            for (var i in e) e.hasOwnProperty(i) && (r[i] = t.util.clone(e[i]));
                            return r;
                        case "Array":
                            return e.slice()
                    }
                    return e
                }
            },
            languages: {
                extend: function(e, n) {
                    var r = t.util.clone(t.languages[e]);
                    for (var i in n) r[i] = n[i];
                    return r
                },
                insertBefore: function(e, n, r, i) {
                    i = i || t.languages;
                    var s = i[e],
                        o = {};
                    for (var u in s)
                        if (s.hasOwnProperty(u)) {
                            if (u == n)
                                for (var a in r) r.hasOwnProperty(a) && (o[a] = r[a]);
                            o[u] = s[u]
                        }
                    return i[e] = o
                },
                DFS: function(e, n) {
                    for (var r in e) {
                        n.call(e, r, e[r]);
                        t.util.type(e) === "Object" && t.languages.DFS(e[r], n)
                    }
                }
            },
            highlightAll: function(e, n) {
                var r = document.querySelectorAll('code[class*="language-"], [class*="language-"] code, code[class*="lang-"], [class*="lang-"] code');
                for (var i = 0, s; s = r[i++];) t.highlightElement(s, e === !0, n)
            },
            highlightElement: function(r, i, s) {
                var o, u, a = r;
                while (a && !e.test(a.className)) a = a.parentNode;
                if (a) {
                    o = (a.className.match(e) || [, ""])[1];
                    u = t.languages[o]
                }
                if (!u) return;
                r.className = r.className.replace(e, "").replace(/\s+/g, " ") + " language-" + o;
                a = r.parentNode;
                /pre/i.test(a.nodeName) && (a.className = a.className.replace(e, "").replace(/\s+/g, " ") + " language-" + o);
                var f = r.textContent;
                if (!f) return;
                f = f.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/\u00a0/g, " ");
                var l = {
                    element: r,
                    language: o,
                    grammar: u,
                    code: f
                };
                t.hooks.run("before-highlight", l);
                if (i && self.Worker) {
                    var c = new Worker(t.filename);
                    c.onmessage = function(e) {
                        l.highlightedCode = n.stringify(JSON.parse(e.data), o);
                        t.hooks.run("before-insert", l);
                        l.element.innerHTML = l.highlightedCode;
                        s && s.call(l.element);
                        t.hooks.run("after-highlight", l)
                    };
                    c.postMessage(JSON.stringify({
                        language: l.language,
                        code: l.code
                    }))
                } else {
                    l.highlightedCode = t.highlight(l.code, l.grammar, l.language);
                    t.hooks.run("before-insert", l);
                    l.element.innerHTML = l.highlightedCode;
                    s && s.call(r);
                    t.hooks.run("after-highlight", l)
                }
            },
            highlight: function(e, r, i) {
                return n.stringify(t.tokenize(e, r), i)
            },
            tokenize: function(e, n, r) {
                var i = t.Token,
                    s = [e],
                    o = n.rest;
                if (o) {
                    for (var u in o) n[u] = o[u];
                    delete n.rest
                }
                e: for (var u in n) {
                    if (!n.hasOwnProperty(u) || !n[u]) continue;
                    var a = n[u],
                        f = a.inside,
                        l = !!a.lookbehind,
                        c = 0;
                    a = a.pattern || a;
                    for (var h = 0; h < s.length; h++) {
                        var p = s[h];
                        if (s.length > e.length) break e;
                        if (p instanceof i) continue;
                        a.lastIndex = 0;
                        var d = a.exec(p);
                        if (d) {
                            l && (c = d[1].length);
                            var v = d.index - 1 + c,
                                d = d[0].slice(c),
                                m = d.length,
                                g = v + m,
                                y = p.slice(0, v + 1),
                                b = p.slice(g + 1),
                                w = [h, 1];
                            y && w.push(y);
                            var E = new i(u, f ? t.tokenize(d, f) : d);
                            w.push(E);
                            b && w.push(b);
                            Array.prototype.splice.apply(s, w)
                        }
                    }
                }
                return s
            },
            hooks: {
                all: {},
                add: function(e, n) {
                    var r = t.hooks.all;
                    r[e] = r[e] || [];
                    r[e].push(n)
                },
                run: function(e, n) {
                    var r = t.hooks.all[e];
                    if (!r || !r.length) return;
                    for (var i = 0, s; s = r[i++];) s(n)
                }
            }
        },
        n = t.Token = function(e, t) {
            this.type = e;
            this.content = t
        };
    n.stringify = function(e, r, i) {
        if (typeof e == "string") return e;
        if (Object.prototype.toString.call(e) == "[object Array]") return e.map(function(t) {
            return n.stringify(t, r, e)
        }).join("");
        var s = {
            type: e.type,
            content: n.stringify(e.content, r, i),
            tag: "span",
            classes: ["token", e.type],
            attributes: {},
            language: r,
            parent: i
        };
        s.type == "comment" && (s.attributes.spellcheck = "true");
        t.hooks.run("wrap", s);
        var o = "";
        for (var u in s.attributes) o += u + '="' + (s.attributes[u] || "") + '"';
        return "<" + s.tag + ' class="' + s.classes.join(" ") + '" ' + o + ">" + s.content + "</" + s.tag + ">"
    };
    if (!self.document) {
        self.addEventListener("message", function(e) {
            var n = JSON.parse(e.data),
                r = n.language,
                i = n.code;
            self.postMessage(JSON.stringify(t.tokenize(i, t.languages[r])));
            self.close()
        }, !1);
        return
    }
    var r = document.getElementsByTagName("script");
    r = r[r.length - 1];
    if (r) {
        t.filename = r.src;
        document.addEventListener && !r.hasAttribute("data-manual") && document.addEventListener("DOMContentLoaded", t.highlightAll)
    }
})();
Prism.hooks.add("after-highlight", function(e) {
    var t = e.element.parentNode;
    if (!t || !/pre/i.test(t.nodeName) || t.className.indexOf("line-numbers") === -1) {
        return
    }
    var n = 1 + e.code.split("\n").length;
    var r;
    lines = new Array(n);
    lines = lines.join("<span></span>");
    r = document.createElement("span");
    r.className = "line-numbers-rows";
    r.innerHTML = lines;
    if (t.hasAttribute("data-start")) {
        t.style.counterReset = "linenumber " + (parseInt(t.getAttribute("data-start"), 10) - 1)
    }
    e.element.appendChild(r)
});
Prism.languages.markup = {
    comment: /&lt;!--[\w\W]*?-->/g,
    prolog: /&lt;\?.+?\?>/,
    doctype: /&lt;!DOCTYPE.+?>/,
    cdata: /&lt;!\[CDATA\[[\w\W]*?]]>/i,
    tag: {
        pattern: /&lt;\/?[\w:-]+\s*(?:\s+[\w:-]+(?:=(?:("|')(\\?[\w\W])*?\1|\w+))?\s*)*\/?>/gi,
        inside: {
            tag: {
                pattern: /^&lt;\/?[\w:-]+/i,
                inside: {
                    punctuation: /^&lt;\/?/,
                    namespace: /^[\w-]+?:/
                }
            },
            "attr-value": {
                pattern: /=(?:('|")[\w\W]*?(\1)|[^\s>]+)/gi,
                inside: {
                    punctuation: /=|>|"/g
                }
            },
            punctuation: /\/?>/g,
            "attr-name": {
                pattern: /[\w:-]+/g,
                inside: {
                    namespace: /^[\w-]+?:/
                }
            }
        }
    },
    entity: /&amp;#?[\da-z]{1,8};/gi
};
Prism.hooks.add("wrap", function(e) {
    e.type === "entity" && (e.attributes.title = e.content.replace(/&amp;/, "&"))
});
Prism.languages.css = {
    comment: /\/\*[\w\W]*?\*\//g,
    atrule: {
        pattern: /@[\w-]+?.*?(;|(?=\s*{))/gi,
        inside: {
            punctuation: /[;:]/g
        }
    },
    url: /url\((["']?).*?\1\)/gi,
    selector: /[^\{\}\s][^\{\};]*(?=\s*\{)/g,
    property: /(\b|\B)[\w-]+(?=\s*:)/ig,
    string: /("|')(\\?.)*?\1/g,
    important: /\B!important\b/gi,
    ignore: /&(lt|gt|amp);/gi,
    punctuation: /[\{\};:]/g
};
Prism.languages.markup && Prism.languages.insertBefore("markup", "tag", {
    style: {
        pattern: /(&lt;|<)style[\w\W]*?(>|&gt;)[\w\W]*?(&lt;|<)\/style(>|&gt;)/ig,
        inside: {
            tag: {
                pattern: /(&lt;|<)style[\w\W]*?(>|&gt;)|(&lt;|<)\/style(>|&gt;)/ig,
                inside: Prism.languages.markup.tag.inside
            },
            rest: Prism.languages.css
        }
    }
});
Prism.languages.css.selector = {
    pattern: /[^\{\}\s][^\{\}]*(?=\s*\{)/g,
    inside: {
        "pseudo-element": /:(?:after|before|first-letter|first-line|selection)|::[-\w]+/g,
        "pseudo-class": /:[-\w]+(?:\(.*\))?/g,
        "class": /\.[-:\.\w]+/g,
        id: /#[-:\.\w]+/g
    }
};
Prism.languages.insertBefore("css", "ignore", {
    hexcode: /#[\da-f]{3,6}/gi,
    entity: /\\[\da-f]{1,8}/gi,
    number: /[\d%\.]+/g,
    "function": /(attr|calc|cross-fade|cycle|element|hsla?|image|lang|linear-gradient|matrix3d|matrix|perspective|radial-gradient|repeating-linear-gradient|repeating-radial-gradient|rgba?|rotatex|rotatey|rotatez|rotate3d|rotate|scalex|scaley|scalez|scale3d|scale|skewx|skewy|skew|steps|translatex|translatey|translatez|translate3d|translate|url|var)/ig
});
Prism.languages.clike = {
    comment: {
        pattern: /(^|[^\\])(\/\*[\w\W]*?\*\/|(^|[^:])\/\/.*?(\r?\n|$))/g,
        lookbehind: !0
    },
    string: /("|')(\\?.)*?\1/g,
    "class-name": {
        pattern: /((?:(?:class|interface|extends|implements|trait|instanceof|new)\s+)|(?:catch\s+\())[a-z0-9_\.\\]+/ig,
        lookbehind: !0,
        inside: {
            punctuation: /(\.|\\)/
        }
    },
    keyword: /\b(if|else|while|do|for|return|in|instanceof|function|new|try|throw|catch|finally|null|break|continue)\b/g,
    "boolean": /\b(true|false)\b/g,
    "function": {
        pattern: /[a-z0-9_]+\(/ig,
        inside: {
            punctuation: /\(/
        }
    },
    number: /\b-?(0x[\dA-Fa-f]+|\d*\.?\d+([Ee]-?\d+)?)\b/g,
    operator: /[-+]{1,2}|!|&lt;=?|>=?|={1,3}|(&amp;){1,2}|\|?\||\?|\*|\/|\~|\^|\%/g,
    ignore: /&(lt|gt|amp);/gi,
    punctuation: /[{}[\];(),.:]/g
};
Prism.languages.javascript = Prism.languages.extend("clike", {
    keyword: /\b(var|let|if|else|while|do|for|return|in|instanceof|function|new|with|typeof|try|throw|catch|finally|null|break|continue)\b/g,
    number: /\b-?(0x[\dA-Fa-f]+|\d*\.?\d+([Ee]-?\d+)?|NaN|-?Infinity)\b/g
});
Prism.languages.insertBefore("javascript", "keyword", {
    regex: {
        pattern: /(^|[^/])\/(?!\/)(\[.+?]|\\.|[^/\r\n])+\/[gim]{0,3}(?=\s*($|[\r\n,.;})]))/g,
        lookbehind: !0
    }
});
Prism.languages.markup && Prism.languages.insertBefore("markup", "tag", {
    script: {
        pattern: /(&lt;|<)script[\w\W]*?(>|&gt;)[\w\W]*?(&lt;|<)\/script(>|&gt;)/ig,
        inside: {
            tag: {
                pattern: /(&lt;|<)script[\w\W]*?(>|&gt;)|(&lt;|<)\/script(>|&gt;)/ig,
                inside: Prism.languages.markup.tag.inside
            },
            rest: Prism.languages.javascript
        }
    }
});
Prism.languages.php = Prism.languages.extend("clike", {
    keyword: /\b(and|or|xor|array|as|break|case|cfunction|class|const|continue|declare|default|die|do|else|elseif|enddeclare|endfor|endforeach|endif|endswitch|endwhile|extends|for|foreach|function|include|include_once|global|if|new|return|static|switch|use|require|require_once|var|while|abstract|interface|public|implements|extends|private|protected|parent|static|throw|null|echo|print|trait|namespace|use|final|yield|goto)\b/ig,
    constant: /[A-Z0-9_]{2,}/g
});
Prism.languages.insertBefore("php", "keyword", {
    deliminator: /(\?>|\?&gt;|&lt;\?php|<\?php)/ig,
    "this": /\$this/,
    variable: /(\$\w+)\b/ig,
    scope: {
        pattern: /\b[a-z0-9_\\]+::/ig,
        inside: {
            keyword: /(static|self|parent)/,
            punctuation: /(::|\\)/
        }
    },
    "package": {
        pattern: /(\\|namespace\s+|use\s+)[a-z0-9_\\]+/ig,
        lookbehind: !0,
        inside: {
            punctuation: /\\/
        }
    }
});
Prism.languages.insertBefore("php", "operator", {
    property: {
        pattern: /(-&gt;)[a-z0-9_]+/ig,
        lookbehind: !0
    }
});
Prism.languages.markup && Prism.languages.insertBefore("php", "comment", {
    markup: {
        pattern: /(\?>|\?&gt;)[\w\W]*?(?=(&lt;\?php|<\?php))/ig,
        lookbehind: !0,
        inside: {
            markup: {
                pattern: /&lt;\/?[\w:-]+\s*[\w\W]*?&gt;/gi,
                inside: Prism.languages.markup.tag.inside
            },
            rest: Prism.languages.php
        }
    }
});

/*!
 * slimScroll
 * Copyright (c) 2011 Piotr Rochala (http://rocha.la)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * Version: 1.2.0
 */
(function(e) {
    jQuery.fn.extend({
        slimScroll: function(h) {
            var a = e.extend({
                width: "auto",
                height: "250px",
                size: "7px",
                color: "#000",
                position: "right",
                distance: "1px",
                start: "top",
                opacity: 0.4,
                alwaysVisible: !1,
                disableFadeOut: !1,
                railVisible: !1,
                railColor: "#333",
                railOpacity: 0.2,
                railDraggable: !0,
                railClass: "slimScrollRail",
                barClass: "slimScrollBar",
                wrapperClass: "slimScrollDiv",
                allowPageScroll: !1,
                wheelStep: 20,
                touchScrollStep: 200
            }, h);
            this.each(function() {
                function s(d) {
                    if (q) {
                        d = d || window.event;
                        var c = 0;
                        d.wheelDelta && (c = -d.wheelDelta /
                            120);
                        d.detail && (c = d.detail / 3);
                        e(d.target || d.srcTarget || d.srcElement).closest("." + a.wrapperClass).is(b.parent()) && k(c, !0);
                        d.preventDefault && !m && d.preventDefault();
                        m || (d.returnValue = !1)
                    }
                }

                function k(d, e, h) {
                    var f = d,
                        g = b.outerHeight() - c.outerHeight();
                    e && (f = parseInt(c.css("top")) + d * parseInt(a.wheelStep) / 100 * c.outerHeight(), f = Math.min(Math.max(f, 0), g), f = 0 < d ? Math.ceil(f) : Math.floor(f), c.css({
                        top: f + "px"
                    }));
                    j = parseInt(c.css("top")) / (b.outerHeight() - c.outerHeight());
                    f = j * (b[0].scrollHeight - b.outerHeight());
                    h &&
                        (f = d, d = f / b[0].scrollHeight * b.outerHeight(), d = Math.min(Math.max(d, 0), g), c.css({
                            top: d + "px"
                        }));
                    b.scrollTop(f);
                    b.trigger("slimscrolling", ~~f);
                    t();
                    n()
                }

                function A() {
                    window.addEventListener ? (this.addEventListener("DOMMouseScroll", s, !1), this.addEventListener("mousewheel", s, !1)) : document.attachEvent("onmousewheel", s)
                }

                function u() {
                    r = Math.max(b.outerHeight() / b[0].scrollHeight * b.outerHeight(), B);
                    c.css({
                        height: r + "px"
                    });
                    var a = r == b.outerHeight() ? "none" : "block";
                    c.css({
                        display: a
                    })
                }

                function t() {
                    u();
                    clearTimeout(x);
                    j == ~~j ? (m = a.allowPageScroll, y != j && b.trigger("slimscroll", 0 == ~~j ? "top" : "bottom")) : m = !1;
                    y = j;
                    r >= b.outerHeight() ? m = !0 : (c.stop(!0, !0).fadeIn("fast"), a.railVisible && g.stop(!0, !0).fadeIn("fast"))
                }

                function n() {
                    a.alwaysVisible || (x = setTimeout(function() {
                        if ((!a.disableFadeOut || !q) && !v && !w) c.fadeOut("slow"), g.fadeOut("slow")
                    }, 1E3))
                }
                var q, v, w, x, z, r, j, y, B = 30,
                    m = !1,
                    b = e(this);
                if (b.parent().hasClass(a.wrapperClass)) {
                    var l = b.scrollTop(),
                        c = b.parent().find("." + a.barClass),
                        g = b.parent().find("." + a.railClass);
                    u();
                    if (e.isPlainObject(h)) {
                        if ("height" in
                            h && "auto" == h.height) {
                            b.parent().css("height", "auto");
                            b.css("height", "auto");
                            var p = b.parent().parent().height();
                            b.parent().css("height", p);
                            b.css("height", p)
                        }
                        if ("scrollTo" in h) l = parseInt(a.scrollTo);
                        else if ("scrollBy" in h) l += parseInt(a.scrollBy);
                        else if ("destroy" in h) {
                            c.remove();
                            g.remove();
                            b.unwrap();
                            return
                        }
                        k(l, !1, !0)
                    }
                } else {
                    a.height = "auto" == a.height ? b.parent().height() : a.height;
                    l = e("<div></div>").addClass(a.wrapperClass).css({
                        position: "relative",
                        overflow: "hidden",
                        width: a.width,
                        height: a.height
                    });
                    b.css({
                        overflow: "hidden",
                        width: a.width,
                        height: a.height
                    });
                    var g = e("<div></div>").addClass(a.railClass).css({
                            width: a.size,
                            height: "100%",
                            position: "absolute",
                            top: 0,
                            display: a.alwaysVisible && a.railVisible ? "block" : "none",
                            "border-radius": a.size,
                            background: a.railColor,
                            opacity: a.railOpacity,
                            zIndex: 90
                        }),
                        c = e("<div></div>").addClass(a.barClass).css({
                            background: a.color,
                            width: a.size,
                            position: "absolute",
                            top: 0,
                            opacity: a.opacity,
                            display: a.alwaysVisible ? "block" : "none",
                            "border-radius": a.size,
                            BorderRadius: a.size,
                            MozBorderRadius: a.size,
                            WebkitBorderRadius: a.size,
                            zIndex: 99
                        }),
                        p = "right" == a.position ? {
                            right: a.distance
                        } : {
                            left: a.distance
                        };
                    g.css(p);
                    c.css(p);
                    b.wrap(l);
                    b.parent().append(c);
                    b.parent().append(g);
                    a.railDraggable && (e.ui && "function" == typeof e.ui.draggable) && c.draggable({
                        axis: "y",
                        containment: "parent",
                        start: function() {
                            w = !0
                        },
                        stop: function() {
                            w = !1;
                            n()
                        },
                        drag: function() {
                            k(0, e(this).position().top, !1)
                        }
                    });
                    g.hover(function() {
                        t()
                    }, function() {
                        n()
                    });
                    c.hover(function() {
                        v = !0
                    }, function() {
                        v = !1
                    });
                    b.hover(function() {
                        q = !0;
                        t();
                        n()
                    }, function() {
                        q = !1;
                        n()
                    });
                    b.bind("touchstart",
                        function(a) {
                            a.originalEvent.touches.length && (z = a.originalEvent.touches[0].pageY)
                        });
                    b.bind("touchmove", function(b) {
                        b.originalEvent.preventDefault();
                        b.originalEvent.touches.length && k((z - b.originalEvent.touches[0].pageY) / a.touchScrollStep, !0)
                    });
                    "bottom" === a.start ? (c.css({
                        top: b.outerHeight() - c.outerHeight()
                    }), k(0, !0)) : "top" !== a.start && (k(e(a.start).position().top, null, !0), a.alwaysVisible || c.hide());
                    A();
                    u()
                }
            });
            return this
        }
    });
    jQuery.fn.extend({
        slimscroll: jQuery.fn.slimScroll
    })
})(jQuery);

/*!
 * @fileOverview TouchSwipe - jQuery Plugin
 * @version 1.6.5
 *
 * @author Matt Bryson http://www.github.com/mattbryson
 * @see https://github.com/mattbryson/TouchSwipe-Jquery-Plugin
 * @see http://labs.skinkers.com/touchSwipe/
 * @see http://plugins.jquery.com/project/touchSwipe
 *
 * Copyright (c) 2010 Matt Bryson
 * Dual licensed under the MIT or GPL Version 2 licenses.
 */
(function(a) {
    if (typeof define === "function" && define.amd && define.amd.jQuery) {
        define(["jquery"], a)
    } else {
        a(jQuery)
    }
}(function(e) {
    var o = "left",
        n = "right",
        d = "up",
        v = "down",
        c = "in",
        w = "out",
        l = "none",
        r = "auto",
        k = "swipe",
        s = "pinch",
        x = "tap",
        i = "doubletap",
        b = "longtap",
        A = "horizontal",
        t = "vertical",
        h = "all",
        q = 10,
        f = "start",
        j = "move",
        g = "end",
        p = "cancel",
        a = "ontouchstart" in window,
        y = "TouchSwipe";
    var m = {
        fingers: 1,
        threshold: 75,
        cancelThreshold: null,
        pinchThreshold: 20,
        maxTimeThreshold: null,
        fingerReleaseThreshold: 250,
        longTapThreshold: 500,
        doubleTapThreshold: 200,
        swipe: null,
        swipeLeft: null,
        swipeRight: null,
        swipeUp: null,
        swipeDown: null,
        swipeStatus: null,
        pinchIn: null,
        pinchOut: null,
        pinchStatus: null,
        click: null,
        tap: null,
        doubleTap: null,
        longTap: null,
        triggerOnTouchEnd: true,
        triggerOnTouchLeave: false,
        allowPageScroll: "auto",
        fallbackToMouseEvents: true,
        excludedElements: "label, button, input, select, textarea, a, .noSwipe"
    };
    e.fn.swipe = function(D) {
        var C = e(this),
            B = C.data(y);
        if (B && typeof D === "string") {
            if (B[D]) {
                return B[D].apply(this, Array.prototype.slice.call(arguments, 1))
            } else {
                e.error("Method " + D + " does not exist on jQuery.swipe")
            }
        } else {
            if (!B && (typeof D === "object" || !D)) {
                return u.apply(this, arguments)
            }
        }
        return C
    };
    e.fn.swipe.defaults = m;
    e.fn.swipe.phases = {
        PHASE_START: f,
        PHASE_MOVE: j,
        PHASE_END: g,
        PHASE_CANCEL: p
    };
    e.fn.swipe.directions = {
        LEFT: o,
        RIGHT: n,
        UP: d,
        DOWN: v,
        IN: c,
        OUT: w
    };
    e.fn.swipe.pageScroll = {
        NONE: l,
        HORIZONTAL: A,
        VERTICAL: t,
        AUTO: r
    };
    e.fn.swipe.fingers = {
        ONE: 1,
        TWO: 2,
        THREE: 3,
        ALL: h
    };

    function u(B) {
        if (B && (B.allowPageScroll === undefined && (B.swipe !== undefined || B.swipeStatus !== undefined))) {
            B.allowPageScroll = l
        }
        if (B.click !== undefined && B.tap === undefined) {
            B.tap = B.click
        }
        if (!B) {
            B = {}
        }
        B = e.extend({}, e.fn.swipe.defaults, B);
        return this.each(function() {
            var D = e(this);
            var C = D.data(y);
            if (!C) {
                C = new z(this, B);
                D.data(y, C)
            }
        })
    }

    function z(a0, aq) {
        var av = (a || !aq.fallbackToMouseEvents),
            G = av ? "touchstart" : "mousedown",
            au = av ? "touchmove" : "mousemove",
            R = av ? "touchend" : "mouseup",
            P = av ? null : "mouseleave",
            az = "touchcancel";
        var ac = 0,
            aL = null,
            Y = 0,
            aX = 0,
            aV = 0,
            D = 1,
            am = 0,
            aF = 0,
            J = null;
        var aN = e(a0);
        var W = "start";
        var T = 0;
        var aM = null;
        var Q = 0,
            aY = 0,
            a1 = 0,
            aa = 0,
            K = 0;
        var aS = null;
        try {
            aN.bind(G, aJ);
            aN.bind(az, a5)
        } catch (ag) {
            e.error("events not supported " + G + "," + az + " on jQuery.swipe")
        }
        this.enable = function() {
            aN.bind(G, aJ);
            aN.bind(az, a5);
            return aN
        };
        this.disable = function() {
            aG();
            return aN
        };
        this.destroy = function() {
            aG();
            aN.data(y, null);
            return aN
        };
        this.option = function(a8, a7) {
            if (aq[a8] !== undefined) {
                if (a7 === undefined) {
                    return aq[a8]
                } else {
                    aq[a8] = a7
                }
            } else {
                e.error("Option " + a8 + " does not exist on jQuery.swipe.options")
            }
            return null
        };

        function aJ(a9) {
            if (ax()) {
                return
            }
            if (e(a9.target).closest(aq.excludedElements, aN).length > 0) {
                return
            }
            var ba = a9.originalEvent ? a9.originalEvent : a9;
            var a8, a7 = a ? ba.touches[0] : ba;
            W = f;
            if (a) {
                T = ba.touches.length
            } else {
                a9.preventDefault()
            }
            ac = 0;
            aL = null;
            aF = null;
            Y = 0;
            aX = 0;
            aV = 0;
            D = 1;
            am = 0;
            aM = af();
            J = X();
            O();
            if (!a || (T === aq.fingers || aq.fingers === h) || aT()) {
                ae(0, a7);
                Q = ao();
                if (T == 2) {
                    ae(1, ba.touches[1]);
                    aX = aV = ap(aM[0].start, aM[1].start)
                }
                if (aq.swipeStatus || aq.pinchStatus) {
                    a8 = L(ba, W)
                }
            } else {
                a8 = false
            }
            if (a8 === false) {
                W = p;
                L(ba, W);
                return a8
            } else {
                ak(true)
            }
            return null
        }

        function aZ(ba) {
            var bd = ba.originalEvent ? ba.originalEvent : ba;
            if (W === g || W === p || ai()) {
                return
            }
            var a9, a8 = a ? bd.touches[0] : bd;
            var bb = aD(a8);
            aY = ao();
            if (a) {
                T = bd.touches.length
            }
            W = j;
            if (T == 2) {
                if (aX == 0) {
                    ae(1, bd.touches[1]);
                    aX = aV = ap(aM[0].start, aM[1].start)
                } else {
                    aD(bd.touches[1]);
                    aV = ap(aM[0].end, aM[1].end);
                    aF = an(aM[0].end, aM[1].end)
                }
                D = a3(aX, aV);
                am = Math.abs(aX - aV)
            }
            if ((T === aq.fingers || aq.fingers === h) || !a || aT()) {
                aL = aH(bb.start, bb.end);
                ah(ba, aL);
                ac = aO(bb.start, bb.end);
                Y = aI();
                aE(aL, ac);
                if (aq.swipeStatus || aq.pinchStatus) {
                    a9 = L(bd, W)
                }
                if (!aq.triggerOnTouchEnd || aq.triggerOnTouchLeave) {
                    var a7 = true;
                    if (aq.triggerOnTouchLeave) {
                        var bc = aU(this);
                        a7 = B(bb.end, bc)
                    }
                    if (!aq.triggerOnTouchEnd && a7) {
                        W = ay(j)
                    } else {
                        if (aq.triggerOnTouchLeave && !a7) {
                            W = ay(g)
                        }
                    }
                    if (W == p || W == g) {
                        L(bd, W)
                    }
                }
            } else {
                W = p;
                L(bd, W)
            }
            if (a9 === false) {
                W = p;
                L(bd, W)
            }
        }

        function I(a7) {
            var a8 = a7.originalEvent;
            if (a) {
                if (a8.touches.length > 0) {
                    C();
                    return true
                }
            }
            if (ai()) {
                T = aa
            }
            a7.preventDefault();
            aY = ao();
            Y = aI();
            if (a6()) {
                W = p;
                L(a8, W)
            } else {
                if (aq.triggerOnTouchEnd || (aq.triggerOnTouchEnd == false && W === j)) {
                    W = g;
                    L(a8, W)
                } else {
                    if (!aq.triggerOnTouchEnd && a2()) {
                        W = g;
                        aB(a8, W, x)
                    } else {
                        if (W === j) {
                            W = p;
                            L(a8, W)
                        }
                    }
                }
            }
            ak(false);
            return null
        }

        function a5() {
            T = 0;
            aY = 0;
            Q = 0;
            aX = 0;
            aV = 0;
            D = 1;
            O();
            ak(false)
        }

        function H(a7) {
            var a8 = a7.originalEvent;
            if (aq.triggerOnTouchLeave) {
                W = ay(g);
                L(a8, W)
            }
        }

        function aG() {
            aN.unbind(G, aJ);
            aN.unbind(az, a5);
            aN.unbind(au, aZ);
            aN.unbind(R, I);
            if (P) {
                aN.unbind(P, H)
            }
            ak(false)
        }

        function ay(bb) {
            var ba = bb;
            var a9 = aw();
            var a8 = aj();
            var a7 = a6();
            if (!a9 || a7) {
                ba = p
            } else {
                if (a8 && bb == j && (!aq.triggerOnTouchEnd || aq.triggerOnTouchLeave)) {
                    ba = g
                } else {
                    if (!a8 && bb == g && aq.triggerOnTouchLeave) {
                        ba = p
                    }
                }
            }
            return ba
        }

        function L(a9, a7) {
            var a8 = undefined;
            if (F() || S()) {
                a8 = aB(a9, a7, k)
            } else {
                if ((M() || aT()) && a8 !== false) {
                    a8 = aB(a9, a7, s)
                }
            }
            if (aC() && a8 !== false) {
                a8 = aB(a9, a7, i)
            } else {
                if (al() && a8 !== false) {
                    a8 = aB(a9, a7, b)
                } else {
                    if (ad() && a8 !== false) {
                        a8 = aB(a9, a7, x)
                    }
                }
            }
            if (a7 === p) {
                a5(a9)
            }
            if (a7 === g) {
                if (a) {
                    if (a9.touches.length == 0) {
                        a5(a9)
                    }
                } else {
                    a5(a9)
                }
            }
            return a8
        }

        function aB(ba, a7, a9) {
            var a8 = undefined;
            if (a9 == k) {
                aN.trigger("swipeStatus", [a7, aL || null, ac || 0, Y || 0, T]);
                if (aq.swipeStatus) {
                    a8 = aq.swipeStatus.call(aN, ba, a7, aL || null, ac || 0, Y || 0, T);
                    if (a8 === false) {
                        return false
                    }
                }
                if (a7 == g && aR()) {
                    aN.trigger("swipe", [aL, ac, Y, T]);
                    if (aq.swipe) {
                        a8 = aq.swipe.call(aN, ba, aL, ac, Y, T);
                        if (a8 === false) {
                            return false
                        }
                    }
                    switch (aL) {
                        case o:
                            aN.trigger("swipeLeft", [aL, ac, Y, T]);
                            if (aq.swipeLeft) {
                                a8 = aq.swipeLeft.call(aN, ba, aL, ac, Y, T)
                            }
                            break;
                        case n:
                            aN.trigger("swipeRight", [aL, ac, Y, T]);
                            if (aq.swipeRight) {
                                a8 = aq.swipeRight.call(aN, ba, aL, ac, Y, T)
                            }
                            break;
                        case d:
                            aN.trigger("swipeUp", [aL, ac, Y, T]);
                            if (aq.swipeUp) {
                                a8 = aq.swipeUp.call(aN, ba, aL, ac, Y, T)
                            }
                            break;
                        case v:
                            aN.trigger("swipeDown", [aL, ac, Y, T]);
                            if (aq.swipeDown) {
                                a8 = aq.swipeDown.call(aN, ba, aL, ac, Y, T)
                            }
                            break
                    }
                }
            }
            if (a9 == s) {
                aN.trigger("pinchStatus", [a7, aF || null, am || 0, Y || 0, T, D]);
                if (aq.pinchStatus) {
                    a8 = aq.pinchStatus.call(aN, ba, a7, aF || null, am || 0, Y || 0, T, D);
                    if (a8 === false) {
                        return false
                    }
                }
                if (a7 == g && a4()) {
                    switch (aF) {
                        case c:
                            aN.trigger("pinchIn", [aF || null, am || 0, Y || 0, T, D]);
                            if (aq.pinchIn) {
                                a8 = aq.pinchIn.call(aN, ba, aF || null, am || 0, Y || 0, T, D)
                            }
                            break;
                        case w:
                            aN.trigger("pinchOut", [aF || null, am || 0, Y || 0, T, D]);
                            if (aq.pinchOut) {
                                a8 = aq.pinchOut.call(aN, ba, aF || null, am || 0, Y || 0, T, D)
                            }
                            break
                    }
                }
            }
            if (a9 == x) {
                if (a7 === p || a7 === g) {
                    clearTimeout(aS);
                    if (V() && !E()) {
                        K = ao();
                        aS = setTimeout(e.proxy(function() {
                            K = null;
                            aN.trigger("tap", [ba.target]);
                            if (aq.tap) {
                                a8 = aq.tap.call(aN, ba, ba.target)
                            }
                        }, this), aq.doubleTapThreshold)
                    } else {
                        K = null;
                        aN.trigger("tap", [ba.target]);
                        if (aq.tap) {
                            a8 = aq.tap.call(aN, ba, ba.target)
                        }
                    }
                }
            } else {
                if (a9 == i) {
                    if (a7 === p || a7 === g) {
                        clearTimeout(aS);
                        K = null;
                        aN.trigger("doubletap", [ba.target]);
                        if (aq.doubleTap) {
                            a8 = aq.doubleTap.call(aN, ba, ba.target)
                        }
                    }
                } else {
                    if (a9 == b) {
                        if (a7 === p || a7 === g) {
                            clearTimeout(aS);
                            K = null;
                            aN.trigger("longtap", [ba.target]);
                            if (aq.longTap) {
                                a8 = aq.longTap.call(aN, ba, ba.target)
                            }
                        }
                    }
                }
            }
            return a8
        }

        function aj() {
            var a7 = true;
            if (aq.threshold !== null) {
                a7 = ac >= aq.threshold
            }
            return a7
        }

        function a6() {
            var a7 = false;
            if (aq.cancelThreshold !== null && aL !== null) {
                a7 = (aP(aL) - ac) >= aq.cancelThreshold
            }
            return a7
        }

        function ab() {
            if (aq.pinchThreshold !== null) {
                return am >= aq.pinchThreshold
            }
            return true
        }

        function aw() {
            var a7;
            if (aq.maxTimeThreshold) {
                if (Y >= aq.maxTimeThreshold) {
                    a7 = false
                } else {
                    a7 = true
                }
            } else {
                a7 = true
            }
            return a7
        }

        function ah(a7, a8) {
            if (aq.allowPageScroll === l || aT()) {
                a7.preventDefault()
            } else {
                var a9 = aq.allowPageScroll === r;
                switch (a8) {
                    case o:
                        if ((aq.swipeLeft && a9) || (!a9 && aq.allowPageScroll != A)) {
                            a7.preventDefault()
                        }
                        break;
                    case n:
                        if ((aq.swipeRight && a9) || (!a9 && aq.allowPageScroll != A)) {
                            a7.preventDefault()
                        }
                        break;
                    case d:
                        if ((aq.swipeUp && a9) || (!a9 && aq.allowPageScroll != t)) {
                            a7.preventDefault()
                        }
                        break;
                    case v:
                        if ((aq.swipeDown && a9) || (!a9 && aq.allowPageScroll != t)) {
                            a7.preventDefault()
                        }
                        break
                }
            }
        }

        function a4() {
            var a8 = aK();
            var a7 = U();
            var a9 = ab();
            return a8 && a7 && a9
        }

        function aT() {
            return !!(aq.pinchStatus || aq.pinchIn || aq.pinchOut)
        }

        function M() {
            return !!(a4() && aT())
        }

        function aR() {
            var ba = aw();
            var bc = aj();
            var a9 = aK();
            var a7 = U();
            var a8 = a6();
            var bb = !a8 && a7 && a9 && bc && ba;
            return bb
        }

        function S() {
            return !!(aq.swipe || aq.swipeStatus || aq.swipeLeft || aq.swipeRight || aq.swipeUp || aq.swipeDown)
        }

        function F() {
            return !!(aR() && S())
        }

        function aK() {
            return ((T === aq.fingers || aq.fingers === h) || !a)
        }

        function U() {
            return aM[0].end.x !== 0
        }

        function a2() {
            return !!(aq.tap)
        }

        function V() {
            return !!(aq.doubleTap)
        }

        function aQ() {
            return !!(aq.longTap)
        }

        function N() {
            if (K == null) {
                return false
            }
            var a7 = ao();
            return (V() && ((a7 - K) <= aq.doubleTapThreshold))
        }

        function E() {
            return N()
        }

        function at() {
            return ((T === 1 || !a) && (isNaN(ac) || ac === 0))
        }

        function aW() {
            return ((Y > aq.longTapThreshold) && (ac < q))
        }

        function ad() {
            return !!(at() && a2())
        }

        function aC() {
            return !!(N() && V())
        }

        function al() {
            return !!(aW() && aQ())
        }

        function C() {
            a1 = ao();
            aa = event.touches.length + 1
        }

        function O() {
            a1 = 0;
            aa = 0
        }

        function ai() {
            var a7 = false;
            if (a1) {
                var a8 = ao() - a1;
                if (a8 <= aq.fingerReleaseThreshold) {
                    a7 = true
                }
            }
            return a7
        }

        function ax() {
            return !!(aN.data(y + "_intouch") === true)
        }

        function ak(a7) {
            if (a7 === true) {
                aN.bind(au, aZ);
                aN.bind(R, I);
                if (P) {
                    aN.bind(P, H)
                }
            } else {
                aN.unbind(au, aZ, false);
                aN.unbind(R, I, false);
                if (P) {
                    aN.unbind(P, H, false)
                }
            }
            aN.data(y + "_intouch", a7 === true)
        }

        function ae(a8, a7) {
            var a9 = a7.identifier !== undefined ? a7.identifier : 0;
            aM[a8].identifier = a9;
            aM[a8].start.x = aM[a8].end.x = a7.pageX || a7.clientX;
            aM[a8].start.y = aM[a8].end.y = a7.pageY || a7.clientY;
            return aM[a8]
        }

        function aD(a7) {
            var a9 = a7.identifier !== undefined ? a7.identifier : 0;
            var a8 = Z(a9);
            a8.end.x = a7.pageX || a7.clientX;
            a8.end.y = a7.pageY || a7.clientY;
            return a8
        }

        function Z(a8) {
            for (var a7 = 0; a7 < aM.length; a7++) {
                if (aM[a7].identifier == a8) {
                    return aM[a7]
                }
            }
        }

        function af() {
            var a7 = [];
            for (var a8 = 0; a8 <= 5; a8++) {
                a7.push({
                    start: {
                        x: 0,
                        y: 0
                    },
                    end: {
                        x: 0,
                        y: 0
                    },
                    identifier: 0
                })
            }
            return a7
        }

        function aE(a7, a8) {
            a8 = Math.max(a8, aP(a7));
            J[a7].distance = a8
        }

        function aP(a7) {
            if (J[a7]) {
                return J[a7].distance
            }
            return undefined
        }

        function X() {
            var a7 = {};
            a7[o] = ar(o);
            a7[n] = ar(n);
            a7[d] = ar(d);
            a7[v] = ar(v);
            return a7
        }

        function ar(a7) {
            return {
                direction: a7,
                distance: 0
            }
        }

        function aI() {
            return aY - Q
        }

        function ap(ba, a9) {
            var a8 = Math.abs(ba.x - a9.x);
            var a7 = Math.abs(ba.y - a9.y);
            return Math.round(Math.sqrt(a8 * a8 + a7 * a7))
        }

        function a3(a7, a8) {
            var a9 = (a8 / a7) * 1;
            return a9.toFixed(2)
        }

        function an() {
            if (D < 1) {
                return w
            } else {
                return c
            }
        }

        function aO(a8, a7) {
            return Math.round(Math.sqrt(Math.pow(a7.x - a8.x, 2) + Math.pow(a7.y - a8.y, 2)))
        }

        function aA(ba, a8) {
            var a7 = ba.x - a8.x;
            var bc = a8.y - ba.y;
            var a9 = Math.atan2(bc, a7);
            var bb = Math.round(a9 * 180 / Math.PI);
            if (bb < 0) {
                bb = 360 - Math.abs(bb)
            }
            return bb
        }

        function aH(a8, a7) {
            var a9 = aA(a8, a7);
            if ((a9 <= 45) && (a9 >= 0)) {
                return o
            } else {
                if ((a9 <= 360) && (a9 >= 315)) {
                    return o
                } else {
                    if ((a9 >= 135) && (a9 <= 225)) {
                        return n
                    } else {
                        if ((a9 > 45) && (a9 < 135)) {
                            return v
                        } else {
                            return d
                        }
                    }
                }
            }
        }

        function ao() {
            var a7 = new Date();
            return a7.getTime()
        }

        function aU(a7) {
            a7 = e(a7);
            var a9 = a7.offset();
            var a8 = {
                left: a9.left,
                right: a9.left + a7.outerWidth(),
                top: a9.top,
                bottom: a9.top + a7.outerHeight()
            };
            return a8
        }

        function B(a7, a8) {
            return (a7.x > a8.left && a7.x < a8.right && a7.y > a8.top && a7.y < a8.bottom)
        }
    }
}));

/*!
 * retina.js v0.0.2, a high-resolution image swapper
 *
 * http://retinajs.com
 */
(function() {
    function t(e) {
        this.path = e;
        var t = this.path.split("."),
            n = t.slice(0, t.length - 1).join("."),
            r = t[t.length - 1];
        this.at_2x_path = n + "@2x." + r
    }

    function n(e) {
        this.el = e, this.path = new t(this.el.getAttribute("src"));
        var n = this;
        this.path.check_2x_variant(function(e) {
            e && n.swap()
        })
    }
    var e = typeof exports == "undefined" ? window : exports;
    e.RetinaImagePath = t, t.confirmed_paths = [], t.prototype.is_external = function() {
        return !!this.path.match(/^https?\:/i) && !this.path.match("//" + document.domain)
    }, t.prototype.check_2x_variant = function(e) {
        var n, r = this;
        if (this.is_external()) return e(!1);
        if (this.at_2x_path in t.confirmed_paths) return e(!0);
        n = new XMLHttpRequest, n.open("HEAD", this.at_2x_path), n.onreadystatechange = function() {
            return n.readyState != 4 ? e(!1) : n.status >= 200 && n.status <= 399 ? (t.confirmed_paths.push(r.at_2x_path), e(!0)) : e(!1)
        }, n.send()
    }, e.RetinaImage = n, n.prototype.swap = function(e) {
        function n() {
            t.el.complete ? (t.el.setAttribute("width", t.el.offsetWidth), t.el.setAttribute("height", t.el.offsetHeight), t.el.setAttribute("src", e)) : setTimeout(n, 5)
        }
        typeof e == "undefined" && (e = this.path.at_2x_path);
        var t = this;
        n()
    }, e.devicePixelRatio > 1 && (window.onload = function() {
        var e = document.getElementsByTagName("img"),
            t = [],
            r, i;
        for (r = 0; r < e.length; r++) i = e[r], t.push(new n(i))
    })
})();