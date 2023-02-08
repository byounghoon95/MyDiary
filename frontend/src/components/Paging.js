const Paging = () => {
  return (
    <div className="page_wrap">
      <div className="page_nation">
        <a className="arrow pprev" href="#">
          {"<<"}
        </a>
        <a className="arrow prev" href="#">
          {"<"}
        </a>
        <a href="#" className="active">
          1
        </a>
        <a href="#">2</a>
        <a href="#">3</a>
        <a href="#">4</a>
        <a href="#">5</a>
        <a className="arrow next" href="#">
          {">"}
        </a>
        <a className="arrow nnext" href="#">
          {">>"}
        </a>
      </div>
    </div>
  );
};

export default Paging;
