<template>
  <div style="text-align: left;">
    <!-- 搜索框 -->
    <div style="text-align: center;">
      <el-row :gutter="20">
        <el-col :span="3"><el-input size="small" v-model="searchData.realName" placeholder="请输入客户姓名"></el-input></el-col>
        <el-col :span="3">
          <el-select v-model="searchData.state" placeholder="请选择房间类型" size="small">
            <el-option 
              v-for="item in states"
              :key="item.state"
              :label="item.label"
              :value="item.state">
            </el-option>
          </el-select> 
        </el-col>
        <el-col :span="2"><el-button size="small" type="warning" @click="search">查询</el-button></el-col>
        <el-col :span="1"><el-button size="small" type="warning" @click="reset">重置</el-button></el-col>
      </el-row>
    </div>

    <!-- 添加 -->
    <div>
      <br>
      <br>
    </div>
    
    <!-- 表格 -->
    <div>
      <el-table
        :data="tableData"
        border>
        <el-table-column prop="customer.realName" label="客户姓名"></el-table-column>
        <el-table-column prop="customer.telephone" label="手机号"></el-table-column>
        <el-table-column prop="room.type.name" label="房间类型"></el-table-column>
        <el-table-column prop="room.number" label="房间号"></el-table-column>
        <el-table-column prop="startDate" label="入住时间"></el-table-column>
        <el-table-column prop="endDate" label="退房时间"></el-table-column>
        <el-table-column prop="price" label="入住费用"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="small" v-if="scope.row.state === 0" @click="checkIn(scope.$index)">入住</el-button>
            <el-button size="small" v-if="scope.row.state === 1" @click="checkOut(scope.$index)">退房</el-button>
            <el-tag :type="'success'" v-if="scope.row.state === 2" disable-transitions>已退房</el-tag>
            <el-tag :type="'error'" v-if="scope.row.state === 3" disable-transitions>用户已取消</el-tag>
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
          <el-input v-model="formData.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.price" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary">确 定</el-button>
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
      states: [
        {
          state: 0,
          label: "待使用"
        },
        {
          state: 1,
          label: "已入住"
        },
        {
          state: 2,
          label: "已退房"
        },
        {
          state: 3,
          label: "已取消"
        },
      ],
      searchData: {
          realName: "",
          state: ""
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
        price: '',
        countRoom: '',
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
      this.axios.get("/order-manage", {params: this.pageInfo})
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
      this.dialogFormVisible = true;
    },
    search() {
      this.refreshTable();
    },
    reset() {
      this.searchData.realName = "";
      this.searchData.state = "";
      this.refreshTable();
    },
    checkIn (index) {
      this.axios.patch("/order-manage", this.tableData[index])
      .then(
        response => {
          this.showMessage(response.data.message,response.data.code);
          this.refreshTable();
        },
        error => {
          this.showMessage("访问服务器异常");
        }
      )
    },
    checkOut (index) {
      console.log(JSON.stringify(this.tableData[index]))
      this.axios.patch("/order-manage", this.tableData[index])
      .then(
        response => {
          this.showMessage(response.data.message,response.data.code);
          this.refreshTable();
        },
        error => {
          this.showMessage("访问服务器异常");
        }
      )
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

    confirmWarning(message) {
      return this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      });
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