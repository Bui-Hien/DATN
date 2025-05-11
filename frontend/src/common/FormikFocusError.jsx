import React, { useEffect } from 'react';
import { useFormikContext } from 'formik';
import lodash from 'lodash';
import { toast } from "react-toastify"

const FormikFocusError = (props) => {
  const {
    errors,
    isSubmitting,
    isValidating

  } = useFormikContext();

  // const { currentRef } = props
  // console.log(errors)
  useEffect(() => {
    if (isSubmitting && !isValidating) {
      let errText;
      let errKey;
      getLeaves(errors).every(item => {
        let currentError = lodash.get(errors, item);
        if (currentError) {
          errText = currentError;
          errKey = item;
          return false;
        }
        return true;
      });

      // toast.warning(errText);
      toast.warning(errText, {
        autoClose: 5555,
      });
      return;

      const errorElement = getErrorElementById(errKey);
      if (errorElement) {
        errorElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
        errorElement.focus({ preventScroll: true });
      }
      // }
    }
  }, [errors, isSubmitting, isValidating]);

  function getErrorElementById(id) {
    return document.getElementById(id);
  }
  return <></>;
};

export default FormikFocusError;

const getLeaves = function (tree) {
  const leaves = [];
  const walk = function (obj, path) {
    path = path || "";
    for (let n in obj) {
      if (obj.hasOwnProperty(n)) {
        if (typeof obj[n] === "object" || obj[n] instanceof Array) {
          if (Number.isInteger(parseInt(n))) {
            if (path === "") {
              walk(obj[n], "[" + n + "]");
            } else {
              walk(obj[n], path + "[" + n + "]");
            }
          } else {
            if (path === "") {
              walk(obj[n], n);
            } else {
              walk(obj[n], path + "." + n);
            }
          }
        }
        else {
          if (path === "") {
            leaves.push(n);
          } else {
            leaves.push(path + "." + n);
          }
        }
      }
    }
  }
  walk(tree);
  return leaves;
}
