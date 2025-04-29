package com.timzaak.devops.extra

transparent case class CodeWrap(code:Int) {
  inline def && (next: => CodeWrap): CodeWrap = {
    if(code == 0) {
      next
    } else {
      this
    }
  }
}

inline def mustOK(codeWrap: CodeWrap): Unit = assert(codeWrap.code == 0)