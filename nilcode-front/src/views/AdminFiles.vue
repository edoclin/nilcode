<template>
    <div>

        <el-upload style="width: 100%"
                   class="upload-demo"
                   drag
                   :action="uploadUrl"
                   name="files"
                   multiple
        >
            <i class="el-icon-upload"/>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <el-divider/>
        <el-row :gutter="10">
            <el-col :xs="{span: 12}" :sm="{span: 4}" :xl="{span: 4}" v-for="(item, index) in files" :key="index">
                <el-card style="min-width: 100%;min-height: 250px;margin: 2% 2%" shadow="hover">
                    <el-image v-show="showImg(item.fileUrl)"
                              style="width: 100%; height: 100px;margin: 0 auto;border-radius: 10px"
                              :src="item.fileUrl"
                              fit="fill"
                    />
                    <div style="padding: 1px">
                        <el-link type="info">{{item.originalFileName}}</el-link>
                        <br>
                        <br>
                    </div>
                    <el-row :gutter="10">
                        <el-col :xs="{span: 24}" :sm="{span: 12}">

                            <el-tooltip class="item" effect="light" content="复制链接" placement="top">
                                <el-button size="mini" type="warning" icon="el-icon-document-copy" circle
                                           v-clipboard:copy="item.fileUrl" v-clipboard:success="handleCopyUrl"/>
                            </el-tooltip>

                        </el-col>
                        <el-col :xs="{span: 24}" :sm="{span: 12}">
                            <el-tooltip class="item" effect="light" content="删除文件" placement="top">
                                <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteFile(item)"
                                           circle/>
                            </el-tooltip>
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>

        </el-row>
        <el-pagination
                small
                layout="prev, pager, next"
                :total="total"
                :hide-on-single-page="true"
                @current-change="handleChangePage"/>

    </div>
</template>

<script>
    import config from "@/config";

    export default {
        name: "AdminFiles",
        data() {
            return {
                //todo :
                uploadUrl: config.fileUploadUrl,
                total: 0,
                files: [],
            }
        },
        mounted() {
            this.initData();
        },
        methods: {
            initData() {
                this.axios.get('files/total').then(value => {
                    this.total = parseInt(value.data.data);
                });
                this.axios.get('files/get/1').then(value => {
                    if (value.data.code === 2001) {
                        this.files = JSON.parse(value.data.data);
                    }
                });
            },
            handleChangePage(page) {
                this.axios.get('files/get/' + page).then(value => {
                    if (value.data.code === 2001) {
                        this.files = JSON.parse(value.data.data);
                    }
                });
            },

            handleCopyUrl() {
                this.$message({
                    message: '复制成功',
                    type: 'success'
                });
            },
            showImg(fileUrl) {
                return fileUrl.toLowerCase().endsWith('jpg') || fileUrl.toLowerCase().endsWith('png')
                    || fileUrl.toLowerCase().endsWith('bmp') || fileUrl.toLowerCase().endsWith('bmp')
                    || fileUrl.toLowerCase().endsWith('jpeg')
            },
            deleteFile(file) {
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {

                    this.axios.get('files/del/' + file.fileId).then(value => {
                        if (value.data.code === 2002) {

                            this.files.splice(this.files.indexOf(file), 1);
                            this.$message({
                                type: 'success',
                                message: '删除成功'
                            });
                        } else {
                            this.$message({
                                type: 'error',
                                message: '服务器繁忙'
                            });
                        }
                    })

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        }

    }
</script>

<style scoped>

</style>
