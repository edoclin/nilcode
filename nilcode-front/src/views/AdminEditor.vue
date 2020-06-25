<template>
    <div style="text-align: left">
        <el-row :gutter="40">
            <el-col :xs="{span: 24}" :sm="{span: 12}" style="margin-bottom: 2%">
                <el-input placeholder="请输入标题" v-model="blog.title" maxlength="36" size="small">
                    <template slot="prepend">标题</template>
                </el-input>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 12}" style="margin-bottom: 2%">
                <el-input placeholder="请输入图片地址" v-model="blog.coverImg" size="small">
                    <template slot="prepend">封面</template>
                </el-input>
            </el-col>

            <el-col :xs="{span: 24}" :sm="{span: 12}" style="margin-bottom: 2%">
                <el-cascader
                        :change-on-select="true"
                        v-model="blog.classifications"
                        @change="handleChangeType"
                        style="width: 100%" size="small"
                        placeholder="分类选择"
                        :options="options"
                        filterable/>
            </el-col>


            <el-col :xs="{span: 24}" :sm="{span: 12}" style="margin-bottom: 2%">
                <el-date-picker
                        style="width: 100%"
                        size="small"
                        v-model="blog.publishedDate"
                        type="date"
                        placeholder="选择日期"/>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 12}">

                <el-tag style="margin: 2% 1%;"
                        :key="tag"
                        v-for="tag in dynamicTags"
                        v-show="tag !== ''"
                        closable
                        :disable-transitions="false"
                        @close="handleClose(tag)"
                        type="success">
                    {{tag}}
                </el-tag>
                <el-input style="margin: 2% 2%"
                          class="input-new-tag"
                          v-if="inputVisible"
                          v-model="inputValue"
                          ref="saveTagInput"
                          size="small"
                          @keyup.enter.native="handleInputConfirm"
                          @blur="handleInputConfirm"
                >
                </el-input>
                <el-button v-else class="button-new-tag" size="small" @click="showInput">添加标签</el-button>
            </el-col>
        </el-row>
        <br>
        <MarkdownPro :autoSave="true" :interval="1000 * 60 * 3" v-model="blog.content" @on-save="handleAutoSave"/>
        <br>
        <el-row :gutter="10">
            <el-col :xs="{span: 24}" :sm="{span: 8, offset: 16}" style="text-align: center">
                <el-button type="warning" @click="handleSave(true)">存为草稿</el-button>
                <el-button type="danger" @click="handleSave(false)">发布文章</el-button>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import {MarkdownPro} from 'vue-meditor'

    export default {
        name: "AdminEditor",
        components: {
            MarkdownPro
        },
        data() {
            return {
                blog: {
                    content: '',
                    title: '',
                    publishedDate: '',
                    coverImg: '',
                    classifications: '',
                },
                options: [],
                dynamicTags: [],
                inputVisible: false,
                inputValue: '',
            }
        },
        mounted() {
            this.updateBlog();
            this.allClassifications();
        },
        methods: {
            updateBlog() {
                if (localStorage.getItem('updateBlogId') !== null && localStorage.getItem('updateBlogId') !== '') {
                    // 文章管理跳转
                    this.axios.get('blog/getBlogVo/' + localStorage.getItem('updateBlogId')).then(value => {
                        if (value.data.code === 2001) {
                            this.blog = JSON.parse(value.data.data);
                            let tags = this.blog.tags.substr(1, this.blog.tags.length - 2);
                            this.blog.classifications = '';
                            this.dynamicTags = tags.split(',');
                        }
                    });
                    localStorage.removeItem('updateBlogId');
                }
            },
            allClassifications() {
                this.axios.get('classifications/all').then(value => {
                    this.options = JSON.parse(value.data.data)
                })
            },
            handleChangeType(type) {
                this.blog.classifications = '';
                type.forEach(value => {
                    this.blog.classifications += value + '/';
                });
            },
            handleSave(draft, autoSave) {
                let blog = this.blog;

                if (blog.title === '' || blog.title === null) {
                    this.$message.warning("请输入标题");
                    return;
                }
                if (blog.content === '' || blog.content === null) {
                    this.$message.warning("请输入文章内容");
                    return;
                }
                if (blog.coverImg === '' || blog.coverImg === null) {
                    this.$message.warning("请输入封面");
                    return;
                }
                if (blog.publishedDate === '' || blog.publishedDate === null) {
                    this.$message.warning("请选择发布日期");
                    return;
                }
                if (blog.classifications === '' || blog.classifications === null) {
                    this.$message.warning("请选择分类");
                    return;
                }
                let time = new Date(this.blog.publishedDate).toLocaleDateString().split('/');
                let timeStr = '' + time[0] + '/';
                timeStr += time[1].length === 1 ? '0' : '';
                timeStr += time[1] + '/';
                timeStr += time[2].length === 1 ? '0' : '';
                timeStr += time[2];
                let params = new URLSearchParams();
                params.append('coverImg', this.blog.coverImg);
                params.append('title', this.blog.title);
                params.append('content', this.blog.content);
                params.append('classifications', this.blog.classifications);
                params.append('tags', this.dynamicTags);
                params.append('publishedDate', timeStr);
                params.append('draft', draft);
                this.axios.post('blog/saveOrUpdate', params).then(value => {
                    if (value.data.code === 2005) {
                        this.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        if (!autoSave) {
                            let temp = this.blog.classifications;
                            this.blog = [];
                            this.blog.classifications = temp;
                        }

                    } else {
                        this.$message({
                            message: '服务器繁忙',
                            type: 'error'
                        });
                    }
                })
            },

            handleAutoSave() {
                if (this.blog.title === '' || this.blog.title === null) {
                    return;
                }
                this.handleSave(true, true);
            },
            handleClose(tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
            },
            showInput() {
                this.inputVisible = true;
                this.$nextTick(() => {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },
            handleInputConfirm() {
                let inputValue = this.inputValue;
                if (inputValue) {
                    this.dynamicTags.push(inputValue);
                }
                this.inputVisible = false;
                this.inputValue = '';
            }
        }
    }
</script>

<style scoped>
    .el-tag + .el-tag {
        margin-left: 10px;
    }

    .button-new-tag {
        margin-left: 10px;
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }

    .input-new-tag {
        width: 90px;
        margin-left: 10px;
        vertical-align: bottom;
    }
</style>
