<template>
    <div>
        <el-row>
            <el-col :xs="{span:24}" :sm="{span: 16,offset: 4}">
                <el-timeline>
                    <el-timeline-item :timestamp="item.date" placement="top" v-for="(item, index) in archive"
                                      :key="index">
                        <el-card shadow="never">
                            <el-row>
                                <el-col :xs="{span: 24}" :sm="{span: 8}" v-for="(innerItem, innerIndex) in item.list"
                                        :key="innerIndex">
                                    <el-card shadow="hover" @click="window.console.log('me')" style="width: 90%;margin: 2% auto;min-height: 120px">
                                        <h4>
                                            <el-link type="danger" :href="`/detail/${innerItem.blogId}`"
                                                     style="text-overflow: ellipsis;overflow: hidden;display: -webkit-box;  -webkit-line-clamp: 2;-webkit-box-orient: vertical"
                                            >{{innerItem.blogName}}
                                            </el-link>
                                        </h4>
                                    </el-card>
                                </el-col>
                            </el-row>
                        </el-card>
                    </el-timeline-item>
                </el-timeline>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Archive",
        data() {
            return {
                archive: [],
            };
        },
        mounted() {
            this.getArchive();
        },

        methods: {
            getArchive() {
                this.axios.get('archive/all').then(value => {
                    if (value.data.code === 2001) {
                        this.archive = JSON.parse(value.data.data);
                    }
                })
            },
        }
    }
</script>

<style scoped>

</style>
