<template>
  <div style="text-align: left;">
    <!-- 搜索框 -->
    <div style="text-align: center;">
      <el-row :gutter="20">
        <el-col :span="3"><el-input size="small" v-model="searchData.realName" placeholder="请输入客户姓名"></el-input></el-col>
        <el-col :span="3"><el-input size="small" v-model="searchData.name" placeholder="请输入客户用户名"></el-input></el-col>
        <el-col :span="3"><el-input size="small" v-model="searchData.telephone" placeholder="请输入客户手机号"></el-input></el-col>
        <el-col :span="2"><el-button size="small" type="warning" @click="search">查询</el-button></el-col>
        <el-col :span="3"><el-button size="small" type="warning" @click="reset">重置</el-button></el-col>
      </el-row>
    </div>

    <!-- 添加 -->
    <div>
      <br>
      <el-row :gutter="20">
        <el-col :span="1.5"><el-button size="small" type="warning" @click="add">添加</el-button></el-col>
        <el-col :span="1.5"><el-button size="small" type="warning" @click="refreshTable">刷新</el-button></el-col>
      </el-row>
      <br>
    </div>
    
    <!-- 表格 -->
    <div>
      <el-table
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column
          prop="realName"
          label="姓名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="name"
          label="用户名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="telephone"
          label="手机号">
        </el-table-column>
        <el-table-column
          prop="balance"
          label="余额">
        </el-table-column>
        <el-table-column
          label="操作">
          <template slot-scope="scope">
            <el-button icon="el-icon-edit"   size="small" circle @click="edit(scope.$index)"></el-button>
            <el-button icon="el-icon-delete" size="small" circle @click="deleteById(scope.$index)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
   
   <!-- 分页 -->
    <div class="block" style="margin-bottom: 0px">
      <el-pagination
        @size-change="onPageSizeChange"
        @current-change="onPageChange"
        @prev-click="prevClick"
        @next-click="nextClick"
        :current-page="pageInfo.pageNo"
        :page-sizes="pageSizes"
        :page-size="pageInfo.pageSize"
        :total="countLine">
        layout="total, sizes, prev, next, jumper"
      </el-pagination>
    </div>

  <!--弹窗  -->
    <el-dialog title="用户信息" width="40%" :visible.sync="dialogFormVisible">
      <el-form :model="formData">
        <el-form-item label="姓名" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.realName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.telephone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="余额" :label-width="formLabelWidth" style="margin-right:30px" v-show="dialogState!='add'">
          <el-row>
              <el-col :span="4" ><div style="text-align: left;"><span>￥{{formData.balance}}</span></div></el-col>
              <el-col :span="8"><el-input type="number" placeholder="请输入充值金额" v-model="delta" autocomplete="off"></el-input></el-col>
              <el-col :span="6"><el-button @click="recharge">充值</el-button></el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>


<script>
import Search from "@/components/Search"
export default {
  components: {
    Search
  },
  data() {
    return {
      delta: 0,
      dialogState:"",
      searchData: {
          realName: '',
          name: '',
          telephone: '',
      },
      pageInfo: {
        pageNo: 1,
        pageSize: 10,
      },
      pageSizes: [
        10,20
      ],
      countLine: 0,
      formLabelWidth: '60px',
      dialogFormVisible: false,
      formData: {
        name: '',
        realName: '',
        telephone: '',
        balance: 0.00,
      },
      tableData: []
    }
  },
  mounted() {
    this.refreshTable()
  },
  methods: {
    refreshTable() {
      this.pageInfo.condition = this.searchData;
      this.axios.get("/customer-manage", {params: this.pageInfo})
      .then(
        response => {
          this.tableData = response.data.body.content;
          this.countLine = response.data.body.totalElements;
        },
        error => {
          this.showMessage("服务器未启动");
        }
      )
    },
    add() {
      this.dialogState = "add";
      this.dialogFormVisible = true;
    },
    recharge(){
      this.formData.balance = parseFloat(this.formData.balance) + parseFloat(this.delta);
      this.delta = 0; 
    },
    search() {
      this.refreshTable();
    },
    reset() {
      this.searchData.name="";
      this.searchData.realName="";
      this.searchData.telephone="";
      this.refreshTable();
    },
    showMessage(message,type="error") {
      this.$notify({
        title: "提示",
        message: message,
        position: 'bottom-right',
        type: type,
        // 弹窗停留时间
        duration: 1000
      });
    },

    edit(index){
      this.dialogState = "edit";
      this.formData = this.tableData[index];
      this.dialogFormVisible = true;
    },
    deleteById(index){
        this.confirmWarning('此操作将永久删除该项, 是否继续?').then(
        () => {
          this.axios.delete("/customer-manage", {params:{"id":this.tableData[index].id}})
          .then(
            response => {
              this.refreshTable();
              this.showMessage(response.data.message,response.data.code);
            },
            error => {
              this.showMessage("删除失败");
            }
          );
        }
      )
    },
    confirmWarning(message) {
      return this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      });
    },
    submitForm(){
      switch (this.dialogState){
        case "add":
          this.axios.post("/customer-manage",this.formData)
          .then(
            response=>{
              this.refreshTable();
              this.showMessage(response.data.message,response.data.code);
            },
            error=>{
              this.showMessage("服务器异常");
            }
          );
          this.dialogFormVisible = false;
          break;
        case "edit":
          this.axios.patch("/customer-manage",this.formData)
          .then(
            response=>{
              this.refreshTable();
              this.showMessage(response.data.message,response.data.code);
            },
            error=>{
              this.showMessage("修改失败");
            }
          );
          this.dialogFormVisible = false;
          break;
      }
    },
    onPageChange(val) {
      this.pageTo(val);
      this.refreshTable();
    },
    onPageSizeChange(val) {
      this.pageInfo.pageSize = val;
      this.refreshTable();
    },
    pageTo(pageNo) {
      this.pageInfo.pageNo = pageNo;
      this.refreshTable();
    },
    prevClick(pageNo){
      this.pageNo = pageNo - 1;
      this.refreshTable();
    },
    nextClick(pageNo){
      this.pageNo = pageNo + 1;
      this.refreshTable();
    }
  }
}
</script>

<style>

</style>