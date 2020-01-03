<template>
  <div style="text-align: left;">
    <!-- 搜索框 -->
    <div style="text-align: center;">
      <el-row :gutter="20">
        <el-col :span="3"><el-input size="small" v-model="searchData.name" placeholder="请输入类型名称"></el-input></el-col>
        <el-col :span="2"><el-button size="small" type="warning" @click="search">查询</el-button></el-col>
        <el-col :span="1"><el-button size="small" type="warning" @click="reset">重置</el-button></el-col>
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
        border>
        <el-table-column
          prop="name"
          label="类型名称">
        </el-table-column>
        <el-table-column
          prop="price"
          label="价格">
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
    <el-dialog title="房间类型信息" width="40%" :visible.sync="dialogFormVisible">
      <el-form :model="formData">
        <el-form-item label="类型名称" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.name" autocomplete="off" size="small"></el-input>
        </el-form-item>
        <el-form-item label="价格" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.price" autocomplete="off" size="small"></el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submitForm" size="small">确 定</el-button>
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
      dialogState:"",
      searchData: {
          name: ""
      },
      pageInfo: {
        pageNo: 1,
        pageSize: 10,
      },
      pageSizes: [
        10,20
      ],
      countLine: 0,
      formLabelWidth: '80px',
      dialogFormVisible: false,
      formData: {
        name: '',
        price: '',
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
      this.axios.get("/type-manage", {params: this.pageInfo})
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
      this.formData.id = "";
      this.formData.name = "";
      this.formData.price = "";
      this.formData.deposit = "";
      this.dialogFormVisible = true;
    },
    search() {
      this.refreshTable();
    },
    reset() {
      this.searchData.name="";
      this.searchData.price="";
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
    showDetail(index){
    },
    edit(index){
      this.dialogState = "edit";
      this.formData = this.tableData[index];
      this.dialogFormVisible = true;
    },
    deleteById(index){
        this.confirmWarning('此操作将永久删除该项, 是否继续?').then(
        () => {
          this.axios.delete("/type-manage", {params:{"id":this.tableData[index].id}})
          .then(
            response => {
              this.refreshTable();
              if(response.data.message != "")
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
          console.log(this.formData);
          this.axios.post("/type-manage",this.formData)
          .then(
            response=>{
              this.refreshTable();
              if(response.data.message != "")
                this.showMessage(response.data.message,response.data.code);
            },
            error=>{
              this.showMessage("服务器异常");
            }
          );
          this.dialogFormVisible = false;
          break;
        case "edit":
          console.log(this.formData);
          this.axios.patch("/type-manage",this.formData)
          .then(
            response=>{
              this.refreshTable();
              if(response.data.message != "")
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