package org.wenchen.demo.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 支持批量函数操作的 BO
 * @author earl
 * @date 2020-07-09
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FuncBO <T>  {

     public FuncBO(Boolean right, ExecFunction func) {
          this.right = right;
          this.func = func;
     }

     public FuncBO(Boolean right, ExecReturnFunction <T> returnFunc) {
          this.right = right;
          this.returnFunc = returnFunc;
     }

     private Boolean right;

     private ExecFunction func;

     private ExecReturnFunction <T>  returnFunc;
}
