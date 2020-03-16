

export const showMessage = function (message,type="error") {
  this.$notify({
    title: "提示",
    message: message,
    position: 'bottom-right',
    type: type,
    // 弹窗停留时间
    duration: 1000
  });
}

export const isEmpty = function (obj) {
  if (obj == undefined || obj == null) {
    return true;
  }
  if ((Array.isArray(obj) || typeof obj === 'string') && obj.length === 0) {
    return true;
  }
  return false;
};

export default {
  showMessage,
  isEmpty
}